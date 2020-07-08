package com.revis.ui.video

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.revis.R
import com.revis.agora.BaseRtmChannelListener
import com.revis.agora.BaseRtmClient
import com.revis.agora.BaseRtmClientListener
import com.revis.databinding.ActivityVideoCallBinding
import com.revis.ui.message.MessageChip
import com.revis.ui.message.MessageChipAdapter
import com.revis.ui.message.Position
import com.revis.ui.shared.BaseActivity
import com.revis.ui.video.AnnotationState.*
import com.revis.ui.video.VideoCallMode.*
import com.revis.utils.*
import io.agora.rtm.SendMessageOptions
import io.agora.rtm.RtmChannelMember
import io.agora.rtm.RtmChannel
import io.agora.rtm.RtmClient
import io.agora.rtm.ResultCallback
import io.agora.rtm.ErrorInfo
import io.agora.rtm.RtmStatusCode
import java.util.UUID
import javax.inject.Inject

class VideoCallActivity : BaseActivity() {

    private lateinit var binding: ActivityVideoCallBinding

    private val args: VideoCallActivityArgs by navArgs()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    @Inject
    lateinit var viewModel: VideoCallViewModel

    @Inject
    lateinit var rtmClientFactory: BaseRtmClient.Factory

    @Inject
    lateinit var sendMessageOptions: SendMessageOptions

    private val rtmClientListener = object : BaseRtmClientListener() { }

    private val rtmChannelListener = object : BaseRtmChannelListener() {

        override fun onTextMessageReceived(text: String, member: RtmChannelMember) {
            runOnUiThread {
                viewModel.addNewMessage(text, member, false)
            }
        }

        override fun onPointerMessageReceived(position: Position) {
            runOnUiThread {
                viewModel.setRemotePointerLocation(position)
            }
        }

        override fun onArrowMessageReceived(step: String, position: Position) {
            runOnUiThread {
                viewModel.setRemoteArrowLocation(position)
            }
        }

        override fun onClearMessageReceived() {
            runOnUiThread {
                viewModel.clearRemoteAnnotation()
            }
        }

        override fun onPauseMessageReceived() {
            runOnUiThread {
                Log.i("Video", "Pause message received")
                viewModel.pauseVideo()
            }
        }

        override fun onResumeMessageReceived() {
            runOnUiThread {
                Log.i("Video", "Resume message received")
                viewModel.resumeVideo()
            }
        }
    }

    private var rtmClient: RtmClient? = null

    private var rtmChannel: RtmChannel? = null

    private lateinit var adapter: MessageChipAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.channel = args.channel
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_video_call
        )
        binding.viewModel = viewModel
        binding.channel = args.channel
        binding.lifecycleOwner = this
        // Setting chronometer font via xml does not work for some reason!
        binding.callTimer.setTypeface(ResourcesCompat.getFont(this, R.font.red_hat))
        binding.appBarLayout.doOnPreDraw {
            viewModel.actionBarHeight.value = binding.appBarLayout.measuredHeight
        }
        viewModel.resetState()

        initAgora()
        initBottomSheet()
        initRecyclerView()
        initListeners()
        subscribeUi()
    }

    private fun initAgora() {
        rtmClient = rtmClientFactory.create(rtmClientListener)
        val randomUUID = UUID.randomUUID().toString().substring(0,6)
        rtmClient?.login(null, randomUUID, object : ResultCallback<Void?> {

            override fun onSuccess(responseInfo: Void?) {
                runOnUiThread {
                    joinChannel()
                }
            }

            override fun onFailure(errorInfo: ErrorInfo) {
                runOnUiThread {
                    showToast("Login failed")
                }
            }
        })
    }

    private fun initBottomSheet() {
        binding.bottomSheet.separator.doOnPreDraw { view ->
            bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root as ConstraintLayout)
            bottomSheetBehavior.peekHeight = view.top
            bottomSheetBehavior.isDraggable = false
            bottomSheetBehavior.addBottomSheetCallback(

                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        with (binding.buttonFab) {
                            if (slideOffset > 0.5f) {
                                if (isShown) hide()
                            } else {
                                if (!isShown) show()
                            }
                        }
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                viewModel.messagesState.value = true
                                binding.buttonFab.hide()
                            }
                            BottomSheetBehavior.STATE_COLLAPSED -> {
                                viewModel.messagesState.value = false
                                binding.appBarLayout.setExpanded(true, true)
                                binding.buttonFab.show()
                            }
                        }
                    }
                }
            )
            subscribePostPeekHeightMeasure()
        }
    }

    private fun initRecyclerView() {
        adapter = MessageChipAdapter()
        binding.bottomSheet.recyclerView.adapter = adapter
        binding.bottomSheet.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initListeners() {
        binding.bottomSheet.buttonSendMessage.setOnClickListener {
            binding.bottomSheet.messageInput.text.toString().let { message ->
                if (!message.isEmpty()) {
                    sendMessage(viewModel.createTextMessage(message))
                    viewModel.addNewMessage(message, null, true)
                    clearMessageInput()
                }
            }
        }

        binding.buttonEndCall.setOnClickListener {
            showEndCallDialog()
        }

        binding.buttonFab.setOnClickListener {
            with (viewModel) {
                currentVideoCallMode.apply {
                    when (value!!) {
                        VIDEO_NORMAL -> {
                            value = VIDEO_PAUSED
                            sendMessage(createPauseMessage())
                            if (currentAnnotationState.value == ANNOTATION_CLEAR) {
                                currentAnnotationState.value = ANNOTATION_ARROW
                                Log.i("video", "Annotation set to ${currentAnnotationState.value.toString()}")
                            }
                        }
                        VIDEO_PAUSED -> {
                            value = VIDEO_NORMAL
                            sendMessage(createResumeMessage())
                        }
                    }
                }
            }
        }

        binding.waitingLayout.buttonShareServiceCallId.setOnClickListener {
            shareServiceCallId()
        }
    }

    private fun shareServiceCallId() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Join my revis servicing call : https://revis.com/?channel=${args.channel} 👷‍♂️")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun subscribeUi() {
        viewModel.localPointerLocation.observe(this, Observer { position ->
            sendMessage(viewModel.createPointerMessage(position))
        })

        viewModel.localArrowLocation.observe(this, Observer { position ->
            sendMessage(viewModel.createArrowMessage("", position))
        })

        viewModel.localAnnotationClear.observe(this, Observer {
            sendMessage(viewModel.createClearMessage())
        })

        viewModel.messageList.observe(this, Observer { messages ->
            adapter.submitList(messages)
            /**
             * Ideally you shouldn't have to call this. Unintended Behaviour.
             */
            adapter.notifyDataSetChanged()
        })

        viewModel.remoteUserJoined.observe(this, Observer { userJoined ->
            if (userJoined) {
                binding.parent.removeView(binding.waitingLayout.root)
                startCallTimer()
            }
        })
    }

    private fun subscribePostPeekHeightMeasure() {
        viewModel.messagesState.observe(this, Observer { enabled ->
            if (enabled) {
                bottomSheetBehavior.expand()
            } else {
                hideKeyboard()
                bottomSheetBehavior.collapse()

            }
        })

        viewModel.currentVideoCallMode.observe(this, Observer { state ->
            with (binding.buttonFab) {
                when (state) {
                    VIDEO_NORMAL -> {
                        disableFullScreen()
                        setIconResource(R.drawable.ic_pause)
                        extend()
                    }
                    else -> {
                        shrink()
                        enableFullScreen()
                        setIconResource(R.drawable.ic_play)
                    }
                }
            }
        })
    }

    private fun startCallTimer() = binding.callTimer.start()

    private fun stopCallTimer() = binding.callTimer.stop()

    private fun getCallDuration(): Long {
        return binding.callTimer.base - SystemClock.elapsedRealtime()
    }

    private fun joinChannel() {
        rtmChannel = rtmClient?.createChannel(
            viewModel.channel,
            rtmChannelListener
        )

        rtmChannel?.join(object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) { }

            override fun onFailure(errorInfo: ErrorInfo) {
                runOnUiThread {
                    showToast("Failed to join channel")
                }
            }
        })
    }

    private fun leaveAndReleaseChannel() {
        rtmChannel?.apply {
            leave(null)
            release()
        }
    }

    private fun clearMessageInput() = binding.bottomSheet.messageInput.text.clear()

    private fun sendMessage(message: String) {
        rtmChannel?.sendMessage(rtmClient?.createMessage(message), sendMessageOptions,
            object : ResultCallback<Void?> {

                /**
                 * Success callback can be used to create delivered tick like WhatsApp.
                 */
                override fun onSuccess(responseInfo: Void?) { }

                override fun onFailure(errorInfo: ErrorInfo) {
                    runOnUiThread {
                        when (errorInfo.errorCode) {
                            RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_TIMEOUT,
                            RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_FAILURE ->
                                showToast("Failed to send message")
                        }
                    }
                }
            }
        )
    }

    private fun enableFullScreen() {
        binding.appBarLayout.setExpanded(false, true)
        bottomSheetBehavior.hide()
    }

    private fun disableFullScreen() {
        binding.appBarLayout.setExpanded(true, true)
        bottomSheetBehavior.collapse()
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.hideSoftInputFromWindow(binding.bottomSheet.messageInput.windowToken, 0)
    }

    private fun showEndCallDialog() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.endCallDialog)
    }


    override fun onBackPressed() {
        if (bottomSheetBehavior.isExpanded()) {
            bottomSheetBehavior.collapse()
        } else if (viewModel.currentVideoCallMode.value == VIDEO_PAUSED) {
            viewModel.currentVideoCallMode.value = VIDEO_NORMAL
            sendMessage(viewModel.createResumeMessage())
        } else if (viewModel.remoteUserJoined.value == false) {
            finish()
        } else {
            showEndCallDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        rtmClient?.logout(null)
        leaveAndReleaseChannel()
        rtmChannel = null

        //Todo: Scope viewModel to activity
        viewModel.remoteUserJoined.value = false
        viewModel.cameraState.value = false
        viewModel.videoState.value = false
        viewModel.micState.value = false
        viewModel.messagesState.value = false
        viewModel.settingsState.value = false
        viewModel.speakerState.value = true
        viewModel.currentVideoCallMode.value = VIDEO_NORMAL
        viewModel.currentAnnotationState.value = ANNOTATION_CLEAR
        viewModel.remoteUserJoined.value = false
        viewModel.localPointerLocation.value = Position(0f, 0f)
        viewModel.remotePointerLocation.value = Position(0f, 0f)
        viewModel.messageList.value = arrayListOf<MessageChip>()
        viewModel.videoQualitySetting.value = VIDEO_QUALITY_HIGH
    }
}
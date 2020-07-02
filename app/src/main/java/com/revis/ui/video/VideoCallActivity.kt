package com.revis.ui.video

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.revis.R
import com.revis.agora.BaseRtmChannelListener
import com.revis.agora.BaseRtmClient
import com.revis.agora.BaseRtmClientListener
import com.revis.databinding.ActivityVideoCallBinding
import com.revis.ui.message.Position
import com.revis.ui.shared.BaseActivity
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
                updateMessages(text, member, false)
            }
        }

        override fun onPointerMessageReceived(position: Position) {
            runOnUiThread {
                viewModel.movePointer(position)
            }
        }

        override fun onClearMessageReceived() {
        }
    }

    private var rtmClient: RtmClient? = null

    private var rtmChannel: RtmChannel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_video_call
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initAgora()
        initBottomSheet()
        initListeners()
        subscribeAnnotation()
    }

    private fun initAgora() {
        rtmClient = rtmClientFactory.create(rtmClientListener)
        val randomUUID = UUID.randomUUID().toString().substring(0,6)
        rtmClient?.login(null, randomUUID, object : ResultCallback<Void?> {

            override fun onSuccess(responseInfo: Void?) {
                joinChannel()
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
            subscribeMessagesButton()
        }
    }

    private fun initListeners() {
        binding.bottomSheet.buttonSendMessage.setOnClickListener {
            binding.bottomSheet.messageInput.text.toString().let { message ->
                if (!message.isEmpty()) {
                    sendMessage(viewModel.createTextMessage(message))
                    updateMessages(message, isSelf = true)
                    clearMessageInput()
                }
            }
        }
    }

    private fun subscribeAnnotation() {
        if (IS_TECHNICAN) {
            viewModel.pointerLocation.observe(this, Observer { position ->
                sendMessage(viewModel.createPointerMessage(position))
            })

            //Todo: Debug
            viewModel.videoState.value = false
            binding.bottomSheet.buttonVideoOff.isEnabled = false
        }

    }

    private fun subscribeMessagesButton() {
        viewModel.messagesState.observe(this, Observer { enabled ->
            if (enabled) {
                bottomSheetBehavior.expand()
            } else {
                bottomSheetBehavior.collapse()
            }
        })
    }

    private fun joinChannel() {
        rtmChannel = rtmClient?.createChannel(
            "remberg",
            rtmChannelListener
        )

        rtmChannel?.join(object : ResultCallback<Void?> {
            override fun onSuccess(responseInfo: Void?) {
                runOnUiThread {
                    showToast("Join channel success")
                }
            }

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

    private fun updateMessages(message: String, member: RtmChannelMember? = null, isSelf: Boolean = false) {
        binding.bottomSheet.messages.append("\n ${if (isSelf) "You" else member?.userId}: $message")
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

    override fun onBackPressed() {
        if (bottomSheetBehavior.isExpanded()) {
            bottomSheetBehavior.collapse()
            //Todo: Add bottomsheet callback
            viewModel.messagesState.value = false
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        rtmClient?.logout(null)
        leaveAndReleaseChannel()
        rtmChannel = null
    }
}
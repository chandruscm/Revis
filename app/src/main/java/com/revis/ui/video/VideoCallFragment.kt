package com.revis.ui.video

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateMargins
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.revis.R
import com.revis.agora.BaseRtcEngine
import com.revis.databinding.FragmentVideoCallBinding
import com.revis.ui.settings.SettingsViewModel
import com.revis.ui.shared.BaseFragment
import com.revis.ui.video.VideoCallMode.VIDEO_NORMAL
import com.revis.ui.video.AnnotationState.ANNOTATION_POINTER
import com.revis.ui.video.AnnotationState.ANNOTATION_ARROW
import com.revis.ui.video.AnnotationState.ANNOTATION_CLEAR
import com.revis.utils.*
import io.agora.rtc.Constants.*
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcChannel
import io.agora.rtc.RtcEngine
import io.agora.rtc.internal.RtcEngineEvent
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import javax.inject.Inject
import javax.inject.Named

class VideoCallFragment : BaseFragment() {

    private lateinit var binding: FragmentVideoCallBinding

    @Inject
    lateinit var viewModel: VideoCallViewModel

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    private lateinit var localVideContainer: FrameLayout
    private lateinit var localSurfaceView: SurfaceView
    private lateinit var remoteVideoContainer: FrameLayout
    private lateinit var remoteSurfaceView: SurfaceView

    @Inject
    lateinit var rtcEngineFactory : BaseRtcEngine.Factory

    @Inject
    @Named("VIDEO_SETTING_LOW")
    lateinit var lowVideoEncoderConfiguration: VideoEncoderConfiguration

    @Inject
    @Named("VIDEO_SETTING_MEDIUM")
    lateinit var mediumVideoEncoderConfiguration: VideoEncoderConfiguration

    @Inject
    @Named("VIDEO_SETTING_HIGH")
    lateinit var highVideoEncoderConfiguration: VideoEncoderConfiguration

    private val rtcEventHandler = object : IRtcEngineEventHandler() {

        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            requireActivity().runOnUiThread { setupRemoteVideo(uid) }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            requireActivity().runOnUiThread { onRemoteUserLeft() }
        }

        override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
            requireActivity().runOnUiThread {
                if (viewModel.currentVideoCallMode.value ?: false == VIDEO_NORMAL) {
                    Log.i("Video", "State : $state Reason $reason")
                    if (state == REMOTE_VIDEO_STATE_STOPPED &&
                        reason == REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED
                    ) {
                        removeRemoteContainer()
                    } else if (state == REMOTE_VIDEO_STATE_DECODING &&
                        reason == REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED
                    ) {
                        showRemoteContainer()
                    }
                }
            }
        }
    }

    private var rtcEngine: RtcEngine? = null

    private var nextLocalArrowIndex = 0
    private var nextRemoteArrowIndex = 0

    private var arrowSizePixels = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoCallBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrowSizePixels = resources.getDimension(R.dimen.arrow_size)

        initAgora()
        setupLocalVideo()
        joinChannel()
        setupAnnotation()
        initListeners()
        subscribeUi()
        subscribeAnnotation()
    }

    private fun initAgora() {
        rtcEngine = rtcEngineFactory.create(rtcEventHandler)

        if (settingsViewModel.isUserTechnician.value ?: false) {
            localVideContainer = binding.videoContainerSmall
            remoteVideoContainer = binding.videoContainerBig
        } else {
            localVideContainer = binding.videoContainerBig
            remoteVideoContainer = binding.videoContainerSmall
        }
    }

    private fun setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        localSurfaceView = RtcEngine.CreateRendererView(requireContext())
        if (settingsViewModel.isUserTechnician.value ?: false) {
            localSurfaceView.setZOrderMediaOverlay(true)
        }
        localVideContainer.addView(localSurfaceView)
        // Initializes the local video view.
        // RENDER_MODE_FIT: Uniformly scale the video until one of its dimension fits the boundary. Areas that are not filled due to the disparity in the aspect ratio are filled with black. 
        rtcEngine?.setupLocalVideo(VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_FILL, 0))
        rtcEngine?.switchCamera()
    }

    /**
     * Parse channel from the URL.
     * Token is not used.
     */
    private fun joinChannel() {
        // Users can only see each other after they join the
        // same channel successfully using the same app id.
        rtcEngine?.joinChannel(null, viewModel.channel, "Extra Optional Data", 0) // if you do not specify the uid, we will generate the uid for you
    }

    private fun leaveChannel() {
        rtcEngine?.leaveChannel()
    }

    private fun setupRemoteVideo(uid: Int) {
        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        remoteVideoContainer.apply {
            if (childCount >= 1) {
                return
            }
        }

        /*
          Creates the video renderer view.
          CreateRendererView returns the SurfaceView type. The operation and layout of the view
          are managed by the app, and the Agora SDK renders the view provided by the app.
          The video display view must be created using this method instead of directly
          calling SurfaceView.
         */
        remoteSurfaceView = RtcEngine.CreateRendererView(requireContext())
        if (!(settingsViewModel.isUserTechnician.value ?: false)) {
            remoteSurfaceView.setZOrderMediaOverlay(true)
        }
        remoteVideoContainer.addView(remoteSurfaceView)
        // Initializes the video view of a remote user.
        rtcEngine?.setupRemoteVideo(VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_FILL, uid))
        remoteSurfaceView.tag = uid // for mark purpose
        viewModel.remoteUserJoined.value = true
    }

    private fun showRemoteContainer() {
        if (settingsViewModel.isUserTechnician.value ?: false) {
            binding.layoutVideoOff.makeGone()
        } else {
            binding.videoContainerSmall.makeVisible()
            if (this::remoteSurfaceView.isInitialized) {
                remoteSurfaceView.makeVisible()
            }
        }
    }

    private fun removeRemoteContainer() {
        if (settingsViewModel.isUserTechnician.value ?: false) {
            binding.layoutVideoOff.makeVisible()
        } else {
            binding.videoContainerSmall.makeInvisible()
            remoteSurfaceView.makeInvisible()
        }
    }

    private fun showLocalContainer() {
        if (settingsViewModel.isUserTechnician.value ?: false) {
            binding.videoContainerSmall.makeVisible()
            if (this::localSurfaceView.isInitialized) {
                localSurfaceView.makeVisible()
            }
        } else {
            binding.layoutVideoOff.makeGone()
        }
    }

    private fun removeLocalContainer() {
        if (settingsViewModel.isUserTechnician.value ?: false) {
            binding.videoContainerSmall.makeInvisible()
            localSurfaceView.makeInvisible()
        } else {
            binding.layoutVideoOff.makeVisible()
        }
    }

    private fun onRemoteUserLeft() {
//        remoteVideoContainer.removeAllViews()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupAnnotation() {
        binding.videoContainerBig.setOnTouchListener { view, motionEvent ->
            val x = motionEvent.x
            val y = motionEvent.y
            with (viewModel) {
                when (motionEvent.action) {
                    MotionEvent.ACTION_MOVE -> {
                        if ((currentAnnotationState.value == ANNOTATION_POINTER) ||
                                (currentVideoCallMode.value == VIDEO_NORMAL)){
                            moveLocalPointer(x, y, false)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        with(displayMetrics()) {
                            Log.i("Video", "First action up")
                            if (currentVideoCallMode.value == VIDEO_NORMAL) {
                                Log.i("Video", "send local pointer location $x $y")
                                moveLocalPointer(x, y, true)
                                viewModel.sendLocalPointerLocation(
                                    x / widthPixels,
                                    y / heightPixels
                                )
                            }
                            if (currentAnnotationState.value == ANNOTATION_POINTER) {
                                /**
                                 * Coordinates need to be scaled to account for the unique screen resolution.
                                 */
                                viewModel.sendLocalPointerLocation(
                                    x / widthPixels,
                                    y / heightPixels
                                )
                            } else if (currentAnnotationState.value == ANNOTATION_ARROW) {
                                Log.i("Video", "x:$x y:$y")
                                addNewLocalArrow(x, y)
                                viewModel.sendLocalArrowLocation(
                                    x / widthPixels,
                                    y / heightPixels
                                )
                            }
                        }
                    }
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun initListeners() {
        binding.buttonPointer.setOnClickListener {
            viewModel.currentAnnotationState.value = ANNOTATION_ARROW
        }

        binding.buttonArrow.setOnClickListener {
            viewModel.currentAnnotationState.value = ANNOTATION_POINTER
        }
    }

    private fun subscribeUi() {
        viewModel.actionBarHeight.observe(viewLifecycleOwner, Observer { height ->
            Log.i("video", "app bar height ${height}")
            (binding.videoContainerSmall.layoutParams as ViewGroup.MarginLayoutParams).apply {
                updateMargins(top = (viewModel.actionBarHeight.value ?: 0) + resources.getDimensionPixelSize(R.dimen.spacing_tiny))
            }
        })

        viewModel.cameraState.observe(viewLifecycleOwner, Observer {
            rtcEngine?.switchCamera()
        })

        viewModel.videoState.observe(viewLifecycleOwner, Observer { disabled ->
            if (disabled) {
                removeLocalContainer()
                rtcEngine?.enableLocalVideo(false)
//                localVideContainer.makeVisible()
            } else {
                showLocalContainer()
                rtcEngine?.enableLocalVideo(true)
//                localVideContainer.makeGone()
            }
        })

        viewModel.micState.observe(viewLifecycleOwner, Observer { disabled ->
            if (disabled) {
                rtcEngine?.enableLocalAudio(false)
            } else {
                rtcEngine?.enableLocalAudio(true)
            }
        })

        viewModel.settingsState.observe(viewLifecycleOwner, Observer { enabled ->
            if (enabled) {
                findNavController().navigate(
                    R.id.action_videoCallFragment2_to_videoCallSettingsDialog
                )
            }
        })

        viewModel.videoQualitySetting.observe(viewLifecycleOwner, Observer { videoQualitySetting ->
            rtcEngine?.setVideoEncoderConfiguration(when (videoQualitySetting) {
                VIDEO_QUALITY_LOW -> lowVideoEncoderConfiguration
                VIDEO_QUALITY_MEDIUM -> mediumVideoEncoderConfiguration
                else -> highVideoEncoderConfiguration
            })
        })

        viewModel.speakerState.observe(viewLifecycleOwner, Observer { enabled ->
            rtcEngine?.setEnableSpeakerphone(enabled)
        })

        viewModel.currentAnnotationState.observe(viewLifecycleOwner, Observer { annotationState ->
            viewModel.sendLocalAnnotationClear()
            when (annotationState) {
                ANNOTATION_POINTER -> {
                    Log.i("Video", "Making local pointer visible")
                    clearLocalArrow()
                    showLocalPointer()
                }
                ANNOTATION_ARROW -> {
                    Log.i("Video", "Clearing local pointer")
                    clearLocalPointer()
                }
                else -> {
                    Log.i("Video", "Clearing local pointer and arrow")
                    clearLocalPointer()
                    clearLocalArrow()
                }
            }
        })

        viewModel.currentVideoCallMode.observe(viewLifecycleOwner, Observer { videoMode ->
            TransitionManager.beginDelayedTransition(binding.parent, Slide(Gravity.START))
            when (videoMode) {
                VIDEO_NORMAL -> {
                    viewModel.currentAnnotationState.value = ANNOTATION_CLEAR
                    if (settingsViewModel.isUserTechnician.value ?: false) {
                        showLocalContainer()
                    } else {
                        showRemoteContainer()
                    }
                    rtcEngine?.enableVideo()
                }
                else -> {
                    if (settingsViewModel.isUserTechnician.value ?: false) {
                        removeLocalContainer()
                    } else {
                        removeRemoteContainer()
                    }
                    rtcEngine?.disableVideo()
                }
            }
            Log.i("Video", "Visibility set to ${binding.videoContainerSmall.visibility}")
        })
    }

    private fun subscribeAnnotation() {
        viewModel.remotePointerLocation.observe(viewLifecycleOwner, Observer { position ->
            with (position) {
                if (viewModel.currentVideoCallMode.value == VIDEO_NORMAL) {
                    moveRemotePointer(x, y, true)
                } else {
                    moveRemotePointer(x, y, false)
                }
            }
        })
        
        viewModel.remoteArrowLocation.observe(viewLifecycleOwner, Observer { position ->
            with (position) {
                addNewRemoteArrow(x, y)
            }
        })

        viewModel.remoteAnnotationClear.observe(viewLifecycleOwner, Observer {
            clearRemoteAnnotation()
        })
    }

    private fun moveLocalPointer(x: Float, y: Float, autoHide: Boolean) {
        if (x != 0f && y != 0f) {
            Log.i("Video", "move local pointer $x $y}")
            TransitionManager.endTransitions(binding.parent)
            with(binding.localPointer) {
                this.x = x - width / 2
                this.y = y - height / 2
                makeVisible()
                if (autoHide) {
                    TransitionManager.beginDelayedTransition(
                        binding.parent,
                        Fade().apply {
                            duration = 1000
                        }
                    )
                    makeGone()
                }
            }
        }
    }

    private fun moveRemotePointer(x: Float, y: Float, autoHide: Boolean) {
        if (x != 0f && y != 0f) {
            Log.i("Video", "move remote pointer $x $y}")
            TransitionManager.endTransitions(binding.parent)
            /**
             * Coordinates need to be scaled to account for the unique screen resolution.
             */
            with(displayMetrics()) {
                with(binding.remotePointer) {
                    makeVisible()
                    this.x = x * widthPixels - width / 2
                    this.y = y * heightPixels - height / 2
                    Log.i(
                        "Video",
                        "Moving remote pointer to ${x * widthPixels - width / 2} ${y * heightPixels - height / 2}"
                    )
                    if (autoHide) {
                        TransitionManager.beginDelayedTransition(
                            binding.parent,
                            Fade().apply {
                                duration = 1000
                            }
                        )
                        makeGone()
                    }
                }
            }
        }
    }
    
    private fun addNewLocalArrow(x: Float, y: Float) {
        if (nextLocalArrowIndex > 2) {
            return
        }
        when (nextLocalArrowIndex) {
            0 -> {
                binding.localArrow1.bottomXY(x, y, arrowSizePixels)
                Log.i("video", "Arrow 1 aaded at x:${binding.localArrow1.x} y:${binding.localArrow1.y}")
                nextLocalArrowIndex ++
            }
            1 -> {
                binding.localArrow2.bottomXY(x, y, arrowSizePixels)
                Log.i("video", "Arrow 2 aaded at x:${binding.localArrow2.x} y:${binding.localArrow2.y}")
                nextLocalArrowIndex ++
            }
            else -> {
                binding.localArrow3.bottomXY(x, y, arrowSizePixels)
                Log.i("video", "Arrow 3 aaded at x:${binding.localArrow3.x} y:${binding.localArrow3.y}")
                nextLocalArrowIndex ++
            }
        }
    }

    private fun addNewRemoteArrow(x: Float, y: Float) {
        with (displayMetrics()) {
            val trueX = x * widthPixels
            val trueY = y * heightPixels
            if (nextRemoteArrowIndex > 2) {
                return
            }
            when (nextRemoteArrowIndex) {
                0 -> {
                    binding.remoteArrow1.bottomXY(trueX, trueY, arrowSizePixels)
                    Log.i(
                        "video",
                        "Arrow 1 aaded at x:${binding.remoteArrow1.x} y:${binding.remoteArrow1.y}"
                    )
                    nextRemoteArrowIndex++
                }
                1 -> {
                    binding.remoteArrow2.bottomXY(trueX, trueY, arrowSizePixels)
                    Log.i(
                        "video",
                        "Arrow 2 aaded at x:${binding.remoteArrow2.x} y:${binding.remoteArrow2.y}"
                    )
                    nextRemoteArrowIndex++
                }
                else -> {
                    binding.remoteArrow3.bottomXY(trueX, trueY, arrowSizePixels)
                    Log.i(
                        "video",
                        "Arrow 3 aaded at x:${binding.remoteArrow3.x} y:${binding.remoteArrow3.y}"
                    )
                    nextRemoteArrowIndex++
                }
            }
        }
    }

    private fun clearRemoteAnnotation() {
        Log.i("Video", "Clear remote annotation called6")
        binding.remotePointer.makeInvisible()
        binding.remoteArrow1.makeGone()
        binding.remoteArrow2.makeGone()
        binding.remoteArrow3.makeGone()
        nextRemoteArrowIndex = 0
    }

    private fun showLocalPointer() {
        binding.localPointer.makeVisible()
    }

    private fun clearLocalPointer() {
        binding.localPointer.makeGone()
    }

    private fun clearLocalArrow() {
        binding.localArrow1.makeGone()
        binding.localArrow2.makeGone()
        binding.localArrow3.makeGone()
        nextLocalArrowIndex = 0
    }

    override fun onDestroy() {
        super.onDestroy()

        leaveChannel()
        /*
          Destroys the RtcEngine instance and releases all resources used by the Agora SDK.

          This method is useful for apps that occasionally make voice or video calls,
          to free up resources for other operations when not making calls.
         */
        RtcEngine.destroy()
        rtcEngine = null
    }
}
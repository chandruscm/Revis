package com.revis.ui.video

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.revis.R
import com.revis.agora.BaseRtcEngine
import com.revis.databinding.FragmentVideoCallBinding
import com.revis.ui.shared.BaseFragment
import com.revis.ui.video.VideoCallState.VIDEO_NORMAL
import com.revis.ui.video.AnnotationState.ANNOTATION_CLEAR
import com.revis.ui.video.AnnotationState.ANNOTATION_POINTER
import com.revis.ui.video.AnnotationState.ANNOTATION_ARROW
import com.revis.utils.*
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import javax.inject.Inject
import javax.inject.Named

class VideoCallFragment : BaseFragment() {

    private lateinit var binding: FragmentVideoCallBinding

    @Inject
    lateinit var viewModel: VideoCallViewModel

    private lateinit var localVideContainer: FrameLayout
    private lateinit var remoteVideoContainer: FrameLayout

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
    }

    private var rtcEngine: RtcEngine? = null

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

        if (IS_TECHNICAN) {
            localVideContainer = binding.videoContainerSmall
            remoteVideoContainer = binding.videoContainerBig
            localVideContainer.makeGone()
        } else {
            localVideContainer = binding.videoContainerBig
            remoteVideoContainer = binding.videoContainerSmall
            remoteVideoContainer.makeGone()
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
        val surfaceView = RtcEngine.CreateRendererView(requireContext())
        surfaceView.setZOrderMediaOverlay(true)
        localVideContainer.addView(surfaceView)
        // Initializes the local video view.
        // RENDER_MODE_FIT: Uniformly scale the video until one of its dimension fits the boundary. Areas that are not filled due to the disparity in the aspect ratio are filled with black. 
        rtcEngine?.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FILL, 0))
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
        val surfaceView = RtcEngine.CreateRendererView(requireContext())
        remoteVideoContainer.addView(surfaceView)
        // Initializes the video view of a remote user.
        rtcEngine?.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FILL, uid))
        surfaceView.tag = uid // for mark purpose
        viewModel.remoteUserJoined.value = true
    }

    private fun onRemoteUserLeft() {
//        remoteVideoContainer.removeAllViews()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupAnnotation() {
        if (IS_TECHNICAN) {
            remoteVideoContainer.setOnTouchListener { view, motionEvent ->
                val x = motionEvent.x
                val y = motionEvent.y
                when (motionEvent.action) {
                    MotionEvent.ACTION_MOVE -> {
                        with (viewModel) {
                            if (currentVideoCallState.value != VIDEO_NORMAL &&
                                    currentAnnotationState.value == ANNOTATION_POINTER) {
                                movePointer(x, y, false)

                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        /**
                         * Coordinates need to be scaled to account for the unique screen resolution.
                         */
                        with (displayMetrics()) {
                            viewModel.movePointer(
                                x / widthPixels,
                                y / heightPixels
                            )
                        }
                    }
                }
                return@setOnTouchListener true
            }
        }
    }

    private fun initListeners() {
        binding.buttonClose.setOnClickListener {
            viewModel.currentVideoCallState.value = VIDEO_NORMAL
        }
    }

    private fun subscribeUi() {
        viewModel.cameraState.observe(viewLifecycleOwner, Observer {
            rtcEngine?.switchCamera()
        })

        viewModel.videoState.observe(viewLifecycleOwner, Observer { disabled ->
            if (disabled) {
                rtcEngine?.enableLocalVideo(false)
//                localVideContainer.makeVisible()
            } else {
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
            when (annotationState) {
                ANNOTATION_POINTER -> {
                    binding.pointer.makeVisible()
                }
                ANNOTATION_ARROW -> {
                    binding.pointer.makeGone()
                }
                else -> {
                    binding.pointer.makeGone()
                }
            }
        })
    }

    private fun subscribeAnnotation() {
        if (!IS_TECHNICAN) {
            viewModel.pointerLocation.observe(viewLifecycleOwner, Observer { position ->
                with (position) {
                    movePointer(x, y, true)
                }
            })
        }
    }

    private fun movePointer(x: Float, y: Float, scale: Boolean) {
        /**
         * Coordinates need to be scaled to account for the unique screen resolution.
         */
        val xScale: Int
        val yScale: Int
        with (displayMetrics()) {
            if (scale) {
                xScale = widthPixels
                yScale = heightPixels
            } else {
                xScale = 1
                yScale = 1
            }
        }
        binding.pointer.x = x * xScale
        binding.pointer.y = y * yScale
        binding.pointer.makeVisible()
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
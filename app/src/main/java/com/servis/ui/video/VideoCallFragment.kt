package com.servis.ui.video

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.servis.agora.BaseRtcEngine
import com.servis.databinding.FragmentVideoCallBinding
import com.servis.ui.shared.BaseFragment
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import javax.inject.Inject

class VideoCallFragment : BaseFragment() {

    private lateinit var binding: FragmentVideoCallBinding

    private val args: VideoCallFragmentArgs by navArgs()

    @Inject
    lateinit var rtcEngineFactory : BaseRtcEngine.Factory

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAgora()
        initListeners()
        setupLocalVideo()
        joinChannel()
    }

    private fun initAgora() {
        rtcEngine = rtcEngineFactory.create(rtcEventHandler)
    }

    private fun initListeners() {
        binding.buttonEndCall.setOnClickListener {
            findNavController()
                .popBackStack()
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
        binding.videoContainerSecondary.addView(surfaceView)
        // Initializes the local video view.
        // RENDER_MODE_FIT: Uniformly scale the video until one of its dimension fits the boundary. Areas that are not filled due to the disparity in the aspect ratio are filled with black. 
        rtcEngine?.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
    }

    /**
     * Parse token and channel from the URL.
     * The token has to be URL encoded to parse + characters.
     */
    private fun joinChannel() {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.
        rtcEngine?.joinChannel(null, args.channel, "Extra Optional Data", 0) // if you do not specify the uid, we will generate the uid for you
    }

    private fun leaveChannel() {
        rtcEngine?.leaveChannel()
    }

    fun setupRemoteVideo(uid: Int) {
        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        binding.videoContainerPrimary.apply {
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
        binding.videoContainerPrimary.addView(surfaceView)
        // Initializes the video view of a remote user.
        rtcEngine?.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))
        surfaceView.tag = uid // for mark purpose
    }

    fun onRemoteUserLeft() {
        binding.videoContainerPrimary.removeAllViews()
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
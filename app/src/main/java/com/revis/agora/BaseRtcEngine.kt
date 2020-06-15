package com.revis.agora

import android.content.Context
import android.util.Log
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoEncoderConfiguration

/**
 * Using factory pattern to become compatible with Dagger.
 * Handles video configuration.
 */
abstract class BaseRtcEngine : RtcEngine() {
    class Factory(
        private val context: Context,
        private val appId: String,
        private val configuration: VideoEncoderConfiguration
    ) {
        fun create(eventHandler: IRtcEngineEventHandler): RtcEngine {
            try {
                return create(context, appId, eventHandler).apply {
                    // In simple use cases, we only need to enable video capturing
                    // and rendering once at the initialization step.
                    // Note: audio recording and playing is enabled by default.
                    enableVideo()
                    // Please go to this page for detailed explanation
                    // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
                    setVideoEncoderConfiguration(configuration)
                }
            } catch (e: Exception) {
                throw RuntimeException(
                    "NEED TO check rtc sdk init fatal error\n" +
                            Log.getStackTraceString(e)
                )
            }
        }
    }
}
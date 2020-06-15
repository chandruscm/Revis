package com.revis.di.module

import android.content.Context
import com.revis.agora.BaseRtcEngine
import com.revis.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import io.agora.rtc.video.VideoEncoderConfiguration

/**
 * Instances that are used for video call.
 */
@SuppressWarnings("unused")
@Module
class VideoCallModule {

    @Provides
    @ApplicationScope
    fun provideVideoEncoderConfiguration() = VideoEncoderConfiguration(
        VideoEncoderConfiguration.VD_960x720,
        VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
        VideoEncoderConfiguration.DEFAULT_MIN_BITRATE,
        VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
    )

    @Provides
    @ApplicationScope
    fun provideBaseRtcEngineFactory(
        context: Context,
        appId: String,
        configuration: VideoEncoderConfiguration
    ) = BaseRtcEngine.Factory(context, appId, configuration)
}
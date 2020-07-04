package com.revis.di.module

import android.content.Context
import com.google.gson.Gson
import com.revis.agora.BaseRtcEngine
import com.revis.di.scope.ActivityScope
import com.revis.di.scope.ApplicationScope
import com.revis.ui.video.VideoCallViewModel
import com.revis.ui.video.VideoCallViewModelFactory
import dagger.Module
import dagger.Provides
import io.agora.rtc.video.VideoEncoderConfiguration
import javax.inject.Named

/**
 * Instances that are used for video call.
 */
@SuppressWarnings("unused")
@Module
class VideoCallModule {

    /**
     * TODO: Try manual resolutions based on screen size.
     */
    @Provides
    @Named("VIDEO_SETTING_LOW")
    @ApplicationScope
    fun provideLowVideoEncoderConfiguration() = VideoEncoderConfiguration(
        VideoEncoderConfiguration.VD_640x360,
        VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
        VideoEncoderConfiguration.DEFAULT_MIN_BITRATE,
        VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
    )

    @Provides
    @Named("VIDEO_SETTING_MEDIUM")
    @ApplicationScope
    fun provideMediumVideoEncoderConfiguration() = VideoEncoderConfiguration(
        VideoEncoderConfiguration.VD_840x480,
        VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_24,
        VideoEncoderConfiguration.DEFAULT_MIN_BITRATE,
        VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
    )

    @Provides
    @Named("VIDEO_SETTING_HIGH")
    @ApplicationScope
    fun provideHightVideoEncoderConfiguration() = VideoEncoderConfiguration(
        VideoEncoderConfiguration.VD_1280x720,
        VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
        VideoEncoderConfiguration.DEFAULT_MIN_BITRATE,
        VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
    )

    @Provides
    @ApplicationScope
    fun provideBaseRtcEngineFactory(
        context: Context,
        appId: String,
        @Named("VIDEO_SETTING_HIGH") configuration: VideoEncoderConfiguration
    ) = BaseRtcEngine.Factory(context, appId, configuration)

    @Provides
    @ApplicationScope
    fun provideVideoCallViewModelFactory(gson: Gson) =
        VideoCallViewModelFactory(gson)

    @Provides
    @ApplicationScope
    fun provideVideoCallViewModel(factory: VideoCallViewModelFactory) =
        factory.create(VideoCallViewModel::class.java)
}
package com.servis.di.module

import android.content.Context
import com.servis.agora.BaseRtmClient
import com.servis.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import io.agora.rtm.SendMessageOptions

/**
 * Instances that are used for messaging.
 */
@SuppressWarnings("unused")
@Module
class MessageModule {

    @Provides
    @ApplicationScope
    fun provideRtmClient(
        context: Context,
        appId: String
    ) = BaseRtmClient.Factory(context, appId)

    @Provides
    @ApplicationScope
    fun provideSendMessageOptions() = SendMessageOptions().apply {
        enableOfflineMessaging = true
    }
}

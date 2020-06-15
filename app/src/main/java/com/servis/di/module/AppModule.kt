package com.servis.di.module

import android.app.Application
import android.content.Context
import com.servis.BuildConfig
import com.servis.ServisApplication
import com.servis.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Main instances that are used through the app.
 */
@SuppressWarnings("unused")
@Module
class AppModule {

    @Provides
    @ApplicationScope
    fun provideApplication(application: Application): ServisApplication {
        return application as ServisApplication
    }

    @Provides
    @ApplicationScope
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @ApplicationScope
    fun provideAppId() = BuildConfig.AGORA_APP_ID
}
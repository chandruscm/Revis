package com.revis.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.revis.BuildConfig
import com.revis.RevisApplication
import com.revis.di.scope.ApplicationScope
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
    fun provideApplication(application: Application): RevisApplication {
        return application as RevisApplication
    }

    @Provides
    @ApplicationScope
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @ApplicationScope
    fun provideAppId() = BuildConfig.AGORA_APP_ID

    @Provides
    @ApplicationScope
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("default", Context.MODE_PRIVATE)
    }
}
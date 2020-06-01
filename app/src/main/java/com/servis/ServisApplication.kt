package com.servis

import com.servis.di.AppComponent
import com.servis.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

@Suppress("unused")
class ServisApplication : DaggerApplication() {

    private lateinit var appComponent: AppComponent

    fun getApplicationComponent() = appComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
            .also { appComponent ->
                appComponent.inject(this)
                this.appComponent = appComponent
            }
    }

}
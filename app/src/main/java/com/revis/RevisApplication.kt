package com.revis

import com.revis.di.AppComponent
import com.revis.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

@Suppress("unused")
class RevisApplication : DaggerApplication() {

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
package com.servis.di

import android.app.Application
import com.servis.ServisApplication
import com.servis.di.builder.ActivityBuilder
import com.servis.di.module.AppModule
import com.servis.di.module.VideoCallModule
import com.servis.di.scope.ApplicationScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule

@ApplicationScope
@Component(
    modules = [
        ActivityBuilder::class,
        AndroidSupportInjectionModule::class,
        ViewModelBuilder::class,
        AppModule::class,
        VideoCallModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: ServisApplication)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}

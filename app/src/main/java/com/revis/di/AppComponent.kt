package com.revis.di

import android.app.Application
import com.revis.RevisApplication
import com.revis.di.builder.ActivityBuilder
import com.revis.di.module.AppModule
import com.revis.di.module.MessageModule
import com.revis.di.module.VideoCallModule
import com.revis.di.scope.ApplicationScope
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
        VideoCallModule::class,
        MessageModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: RevisApplication)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}

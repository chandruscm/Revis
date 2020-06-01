package com.servis.di.builder

import com.servis.di.ViewModelBuilder
import com.servis.di.scope.ActivityScope
import com.servis.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Provides Activity instance using Dagger.
 */
@SuppressWarnings("unused")
@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            FragmentBuilder::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun provideMainActivity(): MainActivity
}
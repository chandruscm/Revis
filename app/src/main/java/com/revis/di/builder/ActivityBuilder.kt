package com.revis.di.builder

import com.revis.di.ViewModelBuilder
import com.revis.di.scope.ActivityScope
import com.revis.ui.MainActivity
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
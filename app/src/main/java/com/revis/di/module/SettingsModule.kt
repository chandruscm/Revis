package com.revis.di.module

import android.content.SharedPreferences
import com.revis.di.scope.ApplicationScope
import com.revis.ui.settings.SettingsViewModel
import com.revis.ui.settings.SettingsViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * Instances that are used for settings.
 */
@SuppressWarnings("unused")
@Module
class SettingsModule {

    @Provides
    @ApplicationScope
    fun provideSettingsViewModelFactory(preferences: SharedPreferences) =
        SettingsViewModelFactory(preferences)

    @Provides
    @ApplicationScope
    fun provideSettingsViewModel(factory: SettingsViewModelFactory) =
        factory.create(SettingsViewModel::class.java)
}
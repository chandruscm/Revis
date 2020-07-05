package com.revis.di.builder

import com.revis.di.scope.FragmentScope
import com.revis.ui.contacts.AllContactsFragment
import com.revis.ui.contacts.CallLogsFragment
import com.revis.ui.dialog.DeepLinkDialogPromptFragment
import com.revis.ui.home.HomeFragment
import com.revis.ui.home.JoinCallOperatorDialog
import com.revis.ui.settings.SettingsFragment
import com.revis.ui.video.AudioSettingsFragment
import com.revis.ui.video.VideoCallFragment
import com.revis.ui.video.VideoCallSettingsDialog
import com.revis.ui.video.VideoSettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Provides Fragment instance using Dagger.
 */
@SuppressWarnings("unused")
@Module
abstract class FragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideJoinCallOperatorDialog(): JoinCallOperatorDialog

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideSettingsFragment(): SettingsFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideAllContactsFragment(): AllContactsFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideCallLogsFragment(): CallLogsFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideVideoCallFragment(): VideoCallFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideDeepLinkDialogPromptFragment(): DeepLinkDialogPromptFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideVideoCallSettingsDialog(): VideoCallSettingsDialog

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideVideoSettingsFragment(): VideoSettingsFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideAudioSettingsFragment(): AudioSettingsFragment
}

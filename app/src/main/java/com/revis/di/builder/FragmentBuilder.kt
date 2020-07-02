package com.revis.di.builder

import com.revis.di.scope.FragmentScope
import com.revis.ui.contacts.AllContactsFragment
import com.revis.ui.contacts.CallLogsFragment
import com.revis.ui.dialog.DeepLinkDialogPromptFragment
import com.revis.ui.home.HomeFragment
import com.revis.ui.video.VideoCallFragment
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
    internal abstract fun provideContactsFragment(): HomeFragment

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
}

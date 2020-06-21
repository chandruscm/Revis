package com.revis.di.builder

import com.revis.di.scope.FragmentScope
import com.revis.ui.contacts.AllContactsFragment
import com.revis.ui.contacts.CallLogsFragments
import com.revis.ui.home.HomeFragment
import com.revis.ui.message.MessageFragment
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
    internal abstract fun provideCallLogsFragment(): CallLogsFragments

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideVideoCallFragment(): VideoCallFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideMessageFragment(): MessageFragment
}

package com.revis.di.builder

import com.revis.di.scope.FragmentScope
import com.revis.ui.contacts.ContactsFragment
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
    internal abstract fun provideContactsFragment(): ContactsFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideVideoCallFragment(): VideoCallFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun provideMessageFragment(): MessageFragment
}

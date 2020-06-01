package com.servis.di.builder

import com.servis.di.scope.FragmentScope
import com.servis.ui.contacts.ContactsFragment
import com.servis.ui.video.VideoCallFragment
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
}

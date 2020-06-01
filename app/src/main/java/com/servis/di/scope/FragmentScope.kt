package com.servis.di.scope

import javax.inject.Scope

/**
 * The Fragment Scope of the dependency tree. Used for dependencies at fragment level.
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope
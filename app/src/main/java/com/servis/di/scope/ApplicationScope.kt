package com.servis.di.scope

import javax.inject.Scope

/**
 * The scope for the entire application runtime.
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope
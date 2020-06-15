package com.revis.di.scope

import javax.inject.Scope

/**
 * The Activity Scope of the dependency tree. Used for dependencies at activity level.
 */
@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope
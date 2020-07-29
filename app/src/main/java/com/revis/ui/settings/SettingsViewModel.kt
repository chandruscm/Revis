package com.revis.ui.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**
 * ViewModel to fetch app-level settings from preferences.
 */
class SettingsViewModel @Inject constructor(
    private val preferences: SharedPreferences
) : ViewModel() {

    private val PREF_USER_TECHNICIAN ="PREF_USER_TECHNICIAN"

    val isUserTechnician = MutableLiveData(getUserRole())

    fun isUserTechnician() = isUserTechnician.value ?: false

    fun setUserRole(technician: Boolean) = preferences.edit {
        putBoolean(PREF_USER_TECHNICIAN, technician)
        isUserTechnician.value = technician
    }

    fun getUserRole() = preferences.getBoolean(PREF_USER_TECHNICIAN, true)
}
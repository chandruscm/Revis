package com.revis.ui.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Factory class to create a VideoCallViewModel.
 */
class VideoCallViewModelFactory @Inject constructor(
    private val gson: Gson
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        VideoCallViewModel(gson) as T
}
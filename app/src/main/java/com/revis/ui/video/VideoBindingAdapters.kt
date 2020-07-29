package com.revis.ui.video

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter

/**
 * Databinding adapters used in the video call ui.
 */
object VideoBindingAdapters {

    @BindingAdapter("checkedButton")
    @JvmStatic
    fun bindIsChecked(radioGroup: RadioGroup, videoQualitySetting: Int) {
        radioGroup.check(radioGroup.getChildAt(videoQualitySetting).id)
    }
}

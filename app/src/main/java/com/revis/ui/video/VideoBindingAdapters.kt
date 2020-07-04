package com.revis.ui.video

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter

object VideoBindingAdapters {

    @BindingAdapter("checkedButton")
    @JvmStatic
    fun bindIsChecked(radioGroup: RadioGroup, videoQualitySetting: Int) {
        radioGroup.check(radioGroup.getChildAt(videoQualitySetting).id)
    }
}

package com.revis.ui.shared

import android.widget.ImageButton
import androidx.databinding.BindingAdapter

object BaseBindingAdapters {

    @BindingAdapter("isSelected")
    @JvmStatic
    fun bindIsSelectedStatus(button: ImageButton, selected: Boolean) {
        button.isSelected = selected
    }
}
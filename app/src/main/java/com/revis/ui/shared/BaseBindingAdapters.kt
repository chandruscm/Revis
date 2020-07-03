package com.revis.ui.shared

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

object BaseBindingAdapters {

    @BindingAdapter("isSelected")
    @JvmStatic
    fun bindIsSelectedStatus(button: ImageButton, selected: Boolean) {
        button.isSelected = selected
    }

    @BindingAdapter("setImageResource")
    @JvmStatic
    fun bindSetImageResource(imageView: ImageView, @DrawableRes drawable: Int) {
        imageView.setImageResource(drawable)
    }

    @BindingAdapter("goneUnless")
    @JvmStatic
    fun goneUnless(view: ViewGroup, condition: Boolean) {
        view.visibility = if (condition) View.VISIBLE else View.GONE
    }
}
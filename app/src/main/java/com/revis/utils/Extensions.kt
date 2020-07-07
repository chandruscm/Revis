package com.revis.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Setting a view's visibility to gone
 */
fun View.makeGone() {
    visibility = View.GONE
}

/**
 * Setting a view's visibility to visible
 */
fun View.makeVisible() {
    visibility = View.VISIBLE
}

/**
 * Show a toast message from a fragment
 */
fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

/**
 * Show a toast message from an Activity
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Flip a boolean in a MutableLiveData
 */
fun MutableLiveData<Boolean>.flipBoolean() {
    this.value?.let { previousValue ->
        this.value = !previousValue
    }
}

/**
 * Expand a BottomSheet
 */
fun BottomSheetBehavior<ConstraintLayout>.expand() {
    this.state = BottomSheetBehavior.STATE_EXPANDED
}

/**
 * Check if BottomSheet is expanded
 */
fun BottomSheetBehavior<ConstraintLayout>.isExpanded(): Boolean {
    return this.state == BottomSheetBehavior.STATE_EXPANDED
}

/**
 * Check if BottomSheet is hidden
 */
fun BottomSheetBehavior<ConstraintLayout>.isHidden(): Boolean {
    return this.state == BottomSheetBehavior.STATE_HIDDEN
}

/**
 * Collapse a BottomSheet
 */
fun BottomSheetBehavior<ConstraintLayout>.collapse() {
    this.state = BottomSheetBehavior.STATE_COLLAPSED
}

/**
 * Hide a BottomSheet
 */
fun BottomSheetBehavior<ConstraintLayout>.hide() {
    this.state = BottomSheetBehavior.STATE_HIDDEN
}


/**
 * Get screen width and height
 */
fun Fragment.displayMetrics(): DisplayMetrics {
    val displayMetrics = DisplayMetrics()
    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}

/**
 * Better way to update lists contained in a MutableLiveData
 */
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

/**
 * Position at bottom center of an ImageView
 */
fun ImageView.bottomXY(x: Float, y: Float, size: Float) {
    this.x = x - size / 2
    this.y = y - size
    visibility = View.VISIBLE
}

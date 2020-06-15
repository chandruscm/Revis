package com.servis.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Setting a view's visibility to gone
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Setting a view's visibility to visible
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Show a toast message from a fragment
 */
fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
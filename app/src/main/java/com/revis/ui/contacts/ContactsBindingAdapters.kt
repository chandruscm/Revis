package com.revis.ui.contacts

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.revis.R
import com.revis.ui.contacts.Contact.Status
import com.revis.ui.contacts.Contact.Status.ONLINE
import com.revis.ui.contacts.Contact.Status.AWAY
import com.revis.ui.contacts.Contact.Status.BUSY

object ContactsBindingAdapters {

    @BindingAdapter("setStatus")
    @JvmStatic
    fun bindSetStatus(textView: TextView, status: Status) {
        val context = textView.context
        val drawable: Drawable?
        val statusText: String
        with (textView) {
            when (status) {
                ONLINE -> {
                    drawable = ContextCompat.getDrawable(context, R.drawable.status_online)
                    statusText = context.getString(R.string.status_online)
                }
                AWAY -> {
                    drawable = ContextCompat.getDrawable(context, R.drawable.status_away)
                    statusText = context.getString(R.string.status_away)
                }
                BUSY -> {
                    drawable = ContextCompat.getDrawable(context, R.drawable.status_busy)
                    statusText = context.getString(R.string.status_busy)
                }
            }
            setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            text = statusText
        }
    }
}


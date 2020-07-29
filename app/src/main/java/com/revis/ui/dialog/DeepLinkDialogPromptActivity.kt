package com.revis.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.navigation.Navigation
import com.revis.R
import com.revis.ui.shared.BaseActivity

/**
 * Activity container for the deep link dialog.
 */
class DeepLinkDialogPromptActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_deep_link_dialog_prompt)
    }

    /**
     * Handle deep links through video call Urls.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Navigation.findNavController(this, R.id.nav_host_fragment)
            .handleDeepLink(intent)
    }
}
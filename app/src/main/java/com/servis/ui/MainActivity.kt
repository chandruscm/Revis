package com.servis.ui

import android.os.Bundle
import com.servis.R
import com.servis.ui.shared.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
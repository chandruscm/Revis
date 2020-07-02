package com.revis.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation.findNavController
import com.revis.R
import com.revis.ui.shared.BaseActivity

class MainActivity : BaseActivity() {

    private val PERMISSIONS_REQUEST_CODE = 99

    private val PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkSelfPermission(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun checkSelfPermission(permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED
                && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                //Todo: Handle userflow if permissions are denied.
            }
        }
    }
}
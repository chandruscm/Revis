package com.revis.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.revis.R
import com.revis.databinding.DialogJoinCallOperatorBinding
import com.revis.ui.shared.BaseDialogFragment

class JoinCallOperatorDialog : BaseDialogFragment() {

    private lateinit var binding: DialogJoinCallOperatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.VideoCallSettingsDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogJoinCallOperatorBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }
}
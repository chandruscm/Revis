package com.revis.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.revis.R
import com.revis.databinding.DialogRecordCallConfirmationBinding
import com.revis.ui.shared.BaseDialogFragment
import com.revis.utils.DESIGN_FAIR_CHANNEL

class RecordCallConfirmationDialog : BaseDialogFragment() {

    private lateinit var binding: DialogRecordCallConfirmationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRecordCallConfirmationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DeepLinkDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.buttonYes.setOnClickListener {
            callContact()
        }

        binding.buttonNo.setOnClickListener {
            callContact()
        }
    }

    private fun callContact() {
        findNavController().navigate(
            RecordCallConfirmationDialogDirections
                .actionRecordCallConfirmationDialogToVideoCallActivity(DESIGN_FAIR_CHANNEL)
        )
    }
}
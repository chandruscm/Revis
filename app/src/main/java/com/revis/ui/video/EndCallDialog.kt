package com.revis.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.revis.R
import com.revis.databinding.DialogEndCallBinding
import com.revis.ui.shared.BaseDialogFragment

class EndCallDialog : BaseDialogFragment() {

    private lateinit var binding: DialogEndCallBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEndCallBinding.inflate(layoutInflater)
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
        binding.buttonNo.setOnClickListener {
            dismiss()
        }

        binding.buttonYes.setOnClickListener {
            dismiss()
            requireActivity().finish()
        }
    }
}
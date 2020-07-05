package com.revis.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
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
        showKeyboard()
    }

    private fun initListeners() {
        binding.buttonJoin.setOnClickListener {
            binding.inputCallId.text?.toString()?.let { callId ->
                if (callId.length == 6) {
                    findNavController().navigate(
                        JoinCallOperatorDialogDirections
                            .actionJoinCallOperatorDialogToVideoCallActivity(
                                callId
                            )
                    )
                    dismiss()
                }
            }
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun showKeyboard() {
        if (binding.inputCallId.requestFocus()) {
            Log.i("JoinCall", "Has Focus")
            dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }
}
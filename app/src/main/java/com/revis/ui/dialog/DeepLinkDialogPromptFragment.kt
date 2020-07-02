package com.revis.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.revis.databinding.FragmentDeepLinkDialogPromptBinding
import com.revis.ui.shared.BaseFragment

class DeepLinkDialogPromptFragment : BaseFragment() {

    private lateinit var binding: FragmentDeepLinkDialogPromptBinding

    private val args: DeepLinkDialogPromptFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeepLinkDialogPromptBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.buttonJoin.setOnClickListener {
            findNavController().navigate(
                DeepLinkDialogPromptFragmentDirections
                    .actionDeepLinkDialogPromptFragmentToVideoCallActivity2(
                        args.channel
                    )
            )
            requireActivity().finish()
        }

        binding.buttonCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
package com.revis.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.revis.databinding.FragmentVideoCallIncomingBinding
import com.revis.ui.shared.BaseFragment
import com.revis.utils.DESIGN_FAIR_CHANNEL

class IncomingCallFragment : BaseFragment() {

    private lateinit var binding: FragmentVideoCallIncomingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoCallIncomingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.button.setOnClickListener {
            findNavController().navigate(
                IncomingCallFragmentDirections
                    .actionIncomingCallFragmentToVideoCallActivity(DESIGN_FAIR_CHANNEL)
            )
        }
    }
}
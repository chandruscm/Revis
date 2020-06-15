package com.servis.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.servis.databinding.FragmentContactsBinding
import com.servis.ui.shared.BaseFragment
import com.servis.utils.show

class ContactsFragment : BaseFragment() {

    private lateinit var binding: FragmentContactsBinding

    private val args: ContactsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyUrlParameters()
        initListeners()
    }

    /**
     * Check if token and channel properties are available.
     * TODO: Use telephone service to initiate video call.
     */
    private fun verifyUrlParameters() {
        if (!args.channel.isEmpty()) {
            binding.buttonStartChat.show()
            binding.buttonStartCall.show()
        }
    }

    private fun initListeners() {
        binding.buttonStartChat.setOnClickListener {
            findNavController()
                .navigate(ContactsFragmentDirections
                    .actionContactsFragmentToMessageFragment(
                        args.channel
                    )
                )
        }

        binding.buttonStartCall.setOnClickListener {
            findNavController()
                .navigate(ContactsFragmentDirections
                    .actionContactsFragmentToVideoCallFragment(
                        args.channel
                    )
                )
        }
    }
}
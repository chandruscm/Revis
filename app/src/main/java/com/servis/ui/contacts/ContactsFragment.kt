package com.servis.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.servis.R
import com.servis.databinding.FragmentContactsBinding
import com.servis.ui.shared.BaseFragment

class ContactsFragment : BaseFragment() {

    private lateinit var binding: FragmentContactsBinding

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

        initListeners()
    }

    private fun initListeners() {
        binding.buttonStartCall.setOnClickListener {
            findNavController()
                .navigate(R.id.action_contactsFragment_to_videoCallFragment)
        }
    }
}
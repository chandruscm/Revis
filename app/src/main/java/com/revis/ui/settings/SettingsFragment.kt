package com.revis.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revis.databinding.FragmentSettingsBinding
import com.revis.ui.shared.BaseFragment
import javax.inject.Inject

/**
 * Debug Settings Fragment for the Homescreen.
 */
class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
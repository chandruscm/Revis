package com.revis.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revis.databinding.FragmentAudioSettingsBinding
import com.revis.ui.shared.BaseFragment
import javax.inject.Inject

class AudioSettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentAudioSettingsBinding

    @Inject
    lateinit var viewModel: VideoCallViewModel

    companion object {
        @JvmStatic
        fun newInstance() = AudioSettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioSettingsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        return binding.root
    }
}
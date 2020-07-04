package com.revis.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revis.databinding.FragmentVideoSettingsBinding
import com.revis.ui.shared.BaseFragment
import javax.inject.Inject

class VideoSettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentVideoSettingsBinding

    @Inject
    lateinit var viewModel: VideoCallViewModel

    companion object {
        @JvmStatic
        fun newInstance() = VideoSettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoSettingsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        return binding.root
    }
}
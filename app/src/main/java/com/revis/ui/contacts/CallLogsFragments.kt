package com.revis.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revis.databinding.FragmentCallLogsBinding
import com.revis.ui.shared.BaseFragment

class CallLogsFragments : BaseFragment() {

    private lateinit var binding: FragmentCallLogsBinding

    /**
     * Navigation components do not yet support view pagers.
     */
    companion object {
        @JvmStatic
        fun newInstance() = CallLogsFragments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCallLogsBinding.inflate(layoutInflater)
        return binding.root
    }
}
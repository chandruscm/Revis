package com.revis.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revis.databinding.FragmentAllContactsBinding
import com.revis.ui.shared.BaseFragment

class AllContactsFragment : BaseFragment() {

    private lateinit var binding: FragmentAllContactsBinding

    /**
     * Navigation components do not yet support view pagers.
     */
    companion object {
        @JvmStatic
        fun newInstance() = AllContactsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllContactsBinding.inflate(layoutInflater)
        return binding.root
    }
}
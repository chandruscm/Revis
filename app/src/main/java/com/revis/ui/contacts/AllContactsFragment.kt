package com.revis.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.revis.databinding.FragmentAllContactsBinding
import com.revis.ui.shared.BaseFragment
import com.revis.utils.getSampleContacts

class AllContactsFragment : BaseFragment() {

    private lateinit var binding: FragmentAllContactsBinding
    private lateinit var adapter: ContactsAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initContactsList()
    }

    private fun initContactsList() {
        adapter = ContactsAdapter()
        adapter.submitList(getSampleContacts(resources))
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
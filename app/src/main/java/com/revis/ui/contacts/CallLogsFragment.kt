package com.revis.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.revis.databinding.FragmentCallLogsBinding
import com.revis.ui.shared.BaseFragment
import com.revis.utils.getSampleCallLogs

/**
 * Tab Fragment on the Homescreen with the list of call logs.
 */
class CallLogsFragment : BaseFragment() {

    private lateinit var binding: FragmentCallLogsBinding
    private lateinit var adapter: CallLogsAdapter

    /**
     * Navigation components do not yet support view pagers.
     */
    companion object {
        @JvmStatic
        fun newInstance() = CallLogsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCallLogsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCallLogsList()
    }

    private fun initCallLogsList() {
        adapter = CallLogsAdapter()
        adapter.submitList(getSampleCallLogs(resources))
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
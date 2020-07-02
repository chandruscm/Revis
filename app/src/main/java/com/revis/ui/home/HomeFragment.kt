package com.revis.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.revis.R
import com.revis.databinding.FragmentHomeBinding
import com.revis.ui.contacts.AllContactsFragment
import com.revis.ui.contacts.CallLogsFragments
import com.revis.ui.shared.BaseFragment
import com.revis.utils.makeVisible

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    private val args: HomeFragmentArgs by navArgs()

    private val adapter by lazy {
        HomeTabAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        with (binding) {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(adapter.getTabTitle(position))
//                tab.setIcon(adapter.getTabIcon(position))
            }.attach()
        }
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
            binding.buttonStartCall.makeVisible()
        }
    }

    private fun initListeners() {
        binding.buttonStartCall.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections
                    .actionHomeFragmentToVideoCallActivity(
                        args.channel
                    )
                )
        }
    }

    inner class HomeTabAdapter : FragmentStateAdapter(this) {

        private val TAB_TITLES = arrayOf(
            R.string.tab_all_technicians,
            R.string.tab_call_logs
        )

        private val TAB_ICONS = arrayOf(
            R.drawable.ic_people,
            R.drawable.ic_history
        )

        fun getTabTitle(position: Int) = TAB_TITLES[position]

        fun getTabIcon(position: Int) = TAB_ICONS[position]

        override fun getItemCount() = TAB_TITLES.size

        override fun createFragment(position: Int) = when (position) {
            0 -> AllContactsFragment.newInstance()
            else -> CallLogsFragments.newInstance()
        }
    }
}
package com.revis.ui.home

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.revis.R
import com.revis.databinding.FragmentHomeBinding
import com.revis.ui.contacts.AllContactsFragment
import com.revis.ui.contacts.CallLogsFragment
import com.revis.ui.settings.SettingsViewModel
import com.revis.ui.shared.BaseFragment
import javax.inject.Inject
import kotlin.random.Random

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewModel: SettingsViewModel

    private lateinit var adapter: HomeTabAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        adapter = HomeTabAdapter()
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

        initAppBar()
        setupFab()
    }

    private fun setupFab() {
        with(binding.buttonStartCall) {
            if (viewModel.isUserTechnician.value ?: false) {
                text = getString(R.string.start_call_technician)
                setOnClickListener {
                    startCallTechnician()
                }
            } else {
                text = getString(R.string.start_call_operator)
                setOnClickListener {
                    showJoinCallOperatorDialog()
                }
            }
        }
    }

    private fun initAppBar() {
        binding.toolbar.inflateMenu(R.menu.menu_home_fragment)
        binding.toolbar.setOnMenuItemClickListener { menu: MenuItem? ->
            if (menu?.itemId == R.id.settingsFragment) {
                findNavController().navigate(
                    R.id.action_homeFragment_to_settingsFragment
                )
            }
            true
        }
    }

    private fun startCallTechnician() {
        val randomChannelId = Random.nextInt(100000, 1000000)
        findNavController().navigate(
            HomeFragmentDirections
                .actionHomeFragmentToVideoCallActivity(
                    randomChannelId.toString()
                )
        )
    }

    private fun showJoinCallOperatorDialog() {
        findNavController().navigate(
            R.id.action_homeFragment_to_joinCallOperatorDialog
        )
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
            else -> CallLogsFragment.newInstance()
        }
    }
}
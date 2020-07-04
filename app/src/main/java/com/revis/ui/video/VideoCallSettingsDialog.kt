package com.revis.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.revis.R
import com.revis.databinding.DialogVideoCallSettingsBinding
import com.revis.ui.shared.BaseDialogFragment
import com.revis.ui.video.AudioSettingsFragment
import com.revis.ui.video.VideoSettingsFragment

class VideoCallSettingsDialog : BaseDialogFragment() {

    private lateinit var binding: DialogVideoCallSettingsBinding

    private val adapter by lazy {
        VideoCallSettingsTabAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.VideoCallSettingsDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogVideoCallSettingsBinding.inflate(layoutInflater)
        with (binding) {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(adapter.getTabTitle(position))
            }.attach()
        }
        return binding.root
    }

    inner class VideoCallSettingsTabAdapter : FragmentStateAdapter(this) {

        private val TAB_TITLES = arrayOf(
            R.string.tab_video_settings,
            R.string.tab_audio_settings
        )

        fun getTabTitle(position: Int) = TAB_TITLES[position]

        override fun getItemCount() = TAB_TITLES.size

        override fun createFragment(position: Int) = when (position) {
            0 -> VideoSettingsFragment.newInstance()
            else -> AudioSettingsFragment.newInstance()
        }
    }
}
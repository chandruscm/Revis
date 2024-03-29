package com.revis.ui.video

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.revis.R
import com.revis.databinding.DialogVideoCallSettingsBinding
import com.revis.ui.shared.BaseDialogFragment
import javax.inject.Inject

/**
 * Settings Dialog in the video call screen.
 */
class VideoCallSettingsDialog : BaseDialogFragment() {

    private lateinit var binding: DialogVideoCallSettingsBinding

    @Inject
    lateinit var viewModel: VideoCallViewModel

    private lateinit var adapter: VideoCallSettingsTabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.VideoCallSettingsDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogVideoCallSettingsBinding.inflate(layoutInflater)
        adapter = VideoCallSettingsTabAdapter()
        with (binding) {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(adapter.getTabTitle(position))
            }.attach()
        }
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.disableSettings()
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
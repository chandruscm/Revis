package com.revis.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.revis.R
import com.revis.databinding.DialogInviteContactsBinding
import com.revis.ui.contacts.ContactsAdapter
import com.revis.ui.shared.BaseDialogFragment
import com.revis.utils.getSampleInvitees

/**
 * Dialog to invite contacts into the video call.
 */
class InviteContactsDialog : BaseDialogFragment() {

    private lateinit var binding: DialogInviteContactsBinding
    private lateinit var adapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogInviteContactsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DeepLinkDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInviteeList()
    }

    private fun initInviteeList() {
        adapter = ContactsAdapter(showOnlineStatus = false)
        adapter.submitList(getSampleInvitees(resources))
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
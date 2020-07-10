package com.revis.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.revis.ui.contacts.ContactsViewHolder.ContactItemHolder
import com.revis.databinding.ListItemContactBinding

class ContactsAdapter(
    private val showOnlineStatus: Boolean,
    private val actionsHandler: ContactsActionsHandler
) : ListAdapter<Contact, ContactsViewHolder>(ConctactsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactItemHolder(
            ListItemContactBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        if (holder is ContactItemHolder) {
            holder.binding.apply {
                contact = getItem(position)
                showOnlineStatus = this@ContactsAdapter.showOnlineStatus
                contactItem.setOnClickListener {
                    actionsHandler.call()
                }
            }
        }
    }
}

sealed class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class ContactItemHolder(
        val binding: ListItemContactBinding
    ) : ContactsViewHolder(binding.root)
}

object ConctactsDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}

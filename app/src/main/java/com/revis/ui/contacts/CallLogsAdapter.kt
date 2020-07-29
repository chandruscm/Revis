package com.revis.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.revis.ui.contacts.CallLogsViewHolder.CallLogItemHolder
import com.revis.databinding.ListItemCallLogBinding

/**
 * Adapter class of the CallLogs RecyclerView.
 */
class CallLogsAdapter : ListAdapter<CallLog, CallLogsViewHolder>(CallLogsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CallLogItemHolder(
            ListItemCallLogBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CallLogsViewHolder, position: Int) {
        if (holder is CallLogItemHolder) {
            holder.binding.apply {
                callLog = getItem(position)
            }
        }
    }
}

sealed class CallLogsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class CallLogItemHolder(
        val binding: ListItemCallLogBinding
    ) : CallLogsViewHolder(binding.root)
}

object CallLogsDiffCallback : DiffUtil.ItemCallback<CallLog>() {

    override fun areItemsTheSame(oldItem: CallLog, newItem: CallLog): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CallLog, newItem: CallLog): Boolean {
        return oldItem == newItem
    }
}

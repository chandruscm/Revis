package com.revis.ui.message

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.revis.databinding.ListItemMessageChipBinding
import com.revis.ui.message.MessageChipViewHolder.MessageChipItemHolder

class MessageChipAdapter : ListAdapter<MessageChip, MessageChipViewHolder>(MessageChipDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageChipViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MessageChipItemHolder(
            ListItemMessageChipBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageChipViewHolder, position: Int) {
        if (holder is MessageChipItemHolder) {
            holder.binding.apply {
                Log.i("MessageChipAdapter", "New message view binded")
                messageChip = getItem(position)
            }
        }
    }
}

sealed class MessageChipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class MessageChipItemHolder(
        val binding: ListItemMessageChipBinding
    ) : MessageChipViewHolder(binding.root)
}

object MessageChipDiffCallBack : DiffUtil.ItemCallback<MessageChip>() {

    override fun areItemsTheSame(oldItem: MessageChip, newItem: MessageChip): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MessageChip, newItem: MessageChip): Boolean {
        return oldItem == newItem
    }
}
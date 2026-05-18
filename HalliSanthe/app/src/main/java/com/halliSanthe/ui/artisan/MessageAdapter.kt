package com.halliSanthe.ui.artisan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.halliSanthe.data.model.Message
import com.halliSanthe.databinding.ItemMessageBinding
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter : ListAdapter<Message, MessageAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.apply {
                tvBuyerName.text = message.buyerName
                tvBuyerPhone.text = message.buyerPhone
                tvProductName.text = "Re: ${message.productName}"
                tvMessageText.text = message.message
                tvTimestamp.text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                    .format(Date(message.timestamp))
                tvStatus.text = message.status.name
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(old: Message, new: Message) = old.id == new.id
        override fun areContentsTheSame(old: Message, new: Message) = old == new
    }
}

package com.example.chatdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatdemo.databinding.ChatMessageItemBinding
import com.example.chatdemo.room.ChatRoomViewModel

class ChatMessagesAdapter(
    private val viewModel: ChatRoomViewModel
) : RecyclerView.Adapter<ChatMessageViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.messages.value?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ChatMessageItemBinding.inflate(layoutInflater, parent, false)
        return ChatMessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }
}

class ChatMessageViewHolder(
    private val binding: ChatMessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: ChatRoomViewModel, position: Int) {
        binding.viewModel = viewModel
        binding.position = position
        binding.executePendingBindings()
    }
}
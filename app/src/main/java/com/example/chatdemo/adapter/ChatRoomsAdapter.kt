package com.example.chatdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatdemo.databinding.ChatRoomItemBinding
import com.example.chatdemo.lobby.LobbyViewModel

class ChatRoomsAdapter(
    private val viewModel: LobbyViewModel
) : RecyclerView.Adapter<ChatRoomViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.rooms.value?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ChatRoomItemBinding.inflate(layoutInflater, parent, false)
        return ChatRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }
}

class ChatRoomViewHolder(
    private val binding: ChatRoomItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: LobbyViewModel, position: Int) {
        binding.viewModel = viewModel
        binding.position = position
        binding.executePendingBindings()
    }
}
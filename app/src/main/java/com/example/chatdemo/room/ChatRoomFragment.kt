package com.example.chatdemo.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatdemo.adapter.ChatMessagesAdapter
import com.example.chatdemo.databinding.ChatRoomFragmentBinding
import com.example.chatdemo.utils.isScrolledToBottom
import com.example.chatdemo.utils.smoothScrollToEnd

class ChatRoomFragment : Fragment() {
    private val args: ChatRoomFragmentArgs by navArgs()
    private val viewModel: ChatRoomViewModel by viewModels {
        ChatRoomViewModelFactory(args.user, args.chatRoom)
    }
    private lateinit var binding: ChatRoomFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val messagesAdapter = ChatMessagesAdapter(viewModel)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding = ChatRoomFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = messagesAdapter

        observeChatMessages(messagesAdapter)
        observeStatus()

        return binding.root
    }

    private fun observeChatMessages(adapter: ChatMessagesAdapter) {
        viewModel.messages.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
            if (binding.recyclerView.isScrolledToBottom()) {
                binding.recyclerView.smoothScrollToEnd()
            }
        })
    }

    private fun observeStatus() {
        viewModel.error.observe(viewLifecycleOwner, Observer { status ->
            Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
        })
    }
}
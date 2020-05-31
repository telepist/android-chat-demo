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
import androidx.recyclerview.widget.RecyclerView
import com.example.chatdemo.adapter.ChatMessagesAdapter
import com.example.chatdemo.databinding.ChatRoomFragmentBinding
import com.example.chatdemo.utils.smoothScrollToEnd

class ChatRoomFragment : Fragment() {
    private val args: ChatRoomFragmentArgs by navArgs()
    private val viewModel: ChatRoomViewModel by viewModels {
        ChatRoomViewModelFactory(args.user, args.chatRoom)
    }
    private lateinit var binding: ChatRoomFragmentBinding
    private var isScrolledToBottom = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val messagesAdapter = ChatMessagesAdapter(viewModel)
        val layoutManager = LinearLayoutManager(requireContext())
        binding = ChatRoomFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = messagesAdapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                isScrolledToBottom =
                    layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1
            }
        })

        observeChatMessages(messagesAdapter)
        observeStatus()

        return binding.root
    }

    private fun observeChatMessages(adapter: ChatMessagesAdapter) {
        viewModel.messages.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
            if (isScrolledToBottom) {
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
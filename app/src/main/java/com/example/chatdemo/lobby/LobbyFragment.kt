package com.example.chatdemo.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatdemo.adapter.ChatRoomsAdapter
import com.example.chatdemo.databinding.LobbyFragmentBinding

class LobbyFragment : Fragment() {
    private val args: LobbyFragmentArgs by navArgs()
    private val viewModel: LobbyViewModel by viewModels {
        LobbyViewModelFactory(args.userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val roomsAdapter = ChatRoomsAdapter(viewModel)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        val binding = LobbyFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = roomsAdapter

        observeChatRooms(roomsAdapter)
        observeStatus()
        observeSelectedRoom()

        return binding.root
    }

    private fun observeChatRooms(roomsAdapter: ChatRoomsAdapter) {
        viewModel.rooms.observe(viewLifecycleOwner, Observer {
            roomsAdapter.notifyDataSetChanged()
        })
    }

    private fun observeStatus() {
        viewModel.error.observe(viewLifecycleOwner, Observer { resId ->
            Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
        })
    }

    private fun observeSelectedRoom() {
        viewModel.selectedRoom.observe(viewLifecycleOwner, Observer { chatRoom ->
            if (chatRoom.isPresent) {
                val action =
                    LobbyFragmentDirections.actionLobbyFragmentToChatRoomFragment(
                        viewModel.user.value!!,  // Shouldn't ever be null at this point
                        chatRoom.get()
                    )
                findNavController().navigate(action)
                viewModel.onNavigationCompleted()
            }
        })
    }
}
package com.example.chatdemo.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.chatdemo.databinding.LoginFragmentBinding

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        observeStatus()
        observeUserId()

        return binding.root
    }

    private fun observeStatus() {
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            val errorText = getString(error.first, error.second)
            Toast.makeText(requireContext(), errorText, Toast.LENGTH_SHORT).show()
        })
    }

    private fun observeUserId() {
        viewModel.userId.observe(viewLifecycleOwner, Observer { userId ->
            Log.d(TAG, "viewModel.userId.observe() userId: $userId")
            val action = LoginFragmentDirections.actionLoginToLobby(userId)
            findNavController().navigate(action)
        })
    }
}

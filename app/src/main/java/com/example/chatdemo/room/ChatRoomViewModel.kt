package com.example.chatdemo.room

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatdemo.R
import com.example.chatdemo.model.ChatMessage
import com.example.chatdemo.model.ChatRoom
import com.example.chatdemo.model.User
import com.example.chatdemo.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

private const val TAG = "ChatRoomViewModel"

class ChatRoomViewModel(
    val user: User,
    val chatRoom: ChatRoom
) : ViewModel(), KoinComponent {

    private val repository: Repository by inject()

    private val _messages = MutableLiveData<List<ChatMessage>>()
    private val _error = MutableLiveData<@StringRes Int>()
    private val _messageInput = MutableLiveData<String>()

    val messages: LiveData<List<ChatMessage>> get() = _messages
    val error: LiveData<Int> get() = _error
    val sendMessage: LiveData<String> get() = _messageInput

    init {
        repository.getMessages(chatRoom)
            .subscribe({ messages ->
                Log.d(TAG, "Messages fetched: $messages")
                _messages.value = messages
            }, { throwable ->
                Log.e(TAG, "Failed fetch messages. ${throwable.localizedMessage}")
                _error.value = R.string.error
            }, {
                Log.d(TAG, "Completed.")
            })
    }

    fun getMessageAt(position: Int) = messages.value?.get(position)

    fun onSendMessage(message: String) {
        repository.sendMessage(chatRoom, user, message)
            .subscribe({
                _messageInput.value = "" // clear message
                Log.d(TAG, "Message send successfully")
            }, { throwable ->
                Log.e(TAG, "Failed send message. ${throwable.localizedMessage}")
                _error.value = R.string.error
            })
    }
}

class ChatRoomViewModelFactory(
    private val user: User,
    private val chatRoom: ChatRoom
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ChatRoomViewModel(user, chatRoom) as T
}
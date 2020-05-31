package com.example.chatdemo.lobby

import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatdemo.R
import com.example.chatdemo.model.ChatRoom
import com.example.chatdemo.model.User
import com.example.chatdemo.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

private const val TAG = "LobbyViewModel"

class LobbyViewModel(
    private val userId: String
) : ViewModel(), KoinComponent {

    val repository: Repository by inject()

    // TODO https://www.bignerdranch.com/blog/livedatareactivestreams-where-rxjava-meets-livedata/
    private val _user = MutableLiveData<User>()
    private val _rooms = MutableLiveData<List<ChatRoom>>()
    private val _error = MutableLiveData<@StringRes Int>()
    private val _selectedRoom = MutableLiveData<Optional<ChatRoom>>()
    private val _roomNameInput = MutableLiveData<String>()

    val user: LiveData<User> get() = _user
    val rooms: LiveData<List<ChatRoom>> get() = _rooms
    val error: LiveData<Int> get() = _error
    val selectedRoom: LiveData<Optional<ChatRoom>> get() = _selectedRoom
    val showChooseNickName = ObservableInt(View.GONE)
    val showChatRooms = ObservableInt(View.GONE)
    val loading = ObservableInt(View.GONE)
    val roomNameInput: LiveData<String> get() = _roomNameInput

    init {
        fetchUser(userId)
        _user.observeForever { // does it leak?
            observeRooms()
        }
    }

    private fun fetchUser(userId: String) {
        Log.d(TAG, "fetchUser() user: $userId")
        loading.set(View.VISIBLE)
        repository.getChatUser(userId)
            .doFinally { loading.set(View.GONE) }
            .subscribe({ user ->
                Log.d(TAG, "User fetched successfully: $user")
                _user.value = user
            }, { throwable ->
                Log.e(TAG, "Failed to fetch user. ${throwable.localizedMessage}")
                _error.value = R.string.error
            }, {
                Log.d(TAG, "User $userId doesn't exist. Ask to create one")
                showChooseNickName.set(View.VISIBLE)
            })
    }

    fun onCreateUser(nickName: String) {
        showChooseNickName.set(View.GONE)
        loading.set(View.VISIBLE)
        repository.createChatUser(userId, nickName)
            .doFinally { loading.set(View.GONE) }
            .subscribe({ user ->
                Log.d(TAG, "User created successfully")
                _user.value = user
            }, { throwable ->
                Log.e(TAG, "Failed to create user. ${throwable.localizedMessage}")
                _error.value = R.string.error
            })
    }

    fun onCreateRoom(roomName: String) {
        loading.set(View.VISIBLE)
        repository.createChatRoom(roomName)
            .doFinally { loading.set(View.GONE) }
            .subscribe({
                Log.d(TAG, "Chat room created successfully")
                _roomNameInput.value = ""
            }, { throwable ->
                Log.e(TAG, "Failed to create chat room. ${throwable.localizedMessage}")
                _error.value = R.string.error
            })
    }

    private fun observeRooms() {
        Log.d(TAG, "fetchRooms()")
        showChatRooms.set(View.VISIBLE)
        repository.getChatRooms()
            .subscribe({ rooms ->
                Log.d(TAG, "Rooms fetched: $rooms")
                _rooms.value = rooms
            }, { throwable ->
                Log.e(TAG, "Failed fetch rooms. ${throwable.localizedMessage}")
                _error.value = R.string.error
            })
    }

    fun getRoomAt(pos: Int) = rooms.value?.get(pos)

    fun onItemClick(position: Int) {
        _selectedRoom.value = Optional.ofNullable(rooms.value?.get(position))
    }

    fun onNavigationCompleted() {
        _selectedRoom.value = Optional.empty()
    }
}

class LobbyViewModelFactory(private val userId: String) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        LobbyViewModel(userId) as T
}
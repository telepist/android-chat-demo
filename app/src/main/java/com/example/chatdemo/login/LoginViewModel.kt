package com.example.chatdemo.login

import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatdemo.R
import com.example.chatdemo.repository.Repository
import com.example.chatdemo.utils.mask
import org.koin.core.KoinComponent
import org.koin.core.inject

private const val TAG = "LoginViewModel"

class LoginViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()

    private val _userId = MutableLiveData<String>()
    private val _error = MutableLiveData<Pair<@StringRes Int, String>>()

    val userId: LiveData<String> get() = _userId
    val error: LiveData<Pair<Int, String>> get() = _error
    val loading = ObservableInt(View.GONE)

    init {
        repository.getLoggedInUser()
            .subscribe({ user ->
                Log.d(TAG, "user ${user.email} already logged in")
                _userId.value = user.uid
            }, { throwable ->
                Log.e(TAG, "error ${throwable.localizedMessage}")
            }, {
                Log.d(TAG, "user not logged in")
            })
    }

    fun onLoginClicked(email: String, password: String) {
        Log.d(TAG, "onLoginClicked() email: $email, password: ${password.mask()}")

        if (email.isBlank()) {
            _error.value = Pair(R.string.empty_email, "")
            return
        }
        if (password.isBlank()) {
            _error.value = Pair(R.string.empty_password, "")
            return
        }
        login(email, password)
    }

    private fun login(email: String, password: String) {
        loading.set(View.VISIBLE)
        repository.login(email, password)
            .doFinally { loading.set(View.GONE) }
            .subscribe({ user ->
                Log.d(TAG, "login successful for user: ${user.email}")
                _userId.value = user.uid
            }, { throwable ->
                Log.e(TAG, "login error: $throwable")
                _error.value = Pair(R.string.login_failed, throwable.localizedMessage)
            })
    }
}


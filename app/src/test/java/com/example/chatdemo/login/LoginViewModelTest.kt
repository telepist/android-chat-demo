package com.example.chatdemo.login

import com.example.chatdemo.BaseChatRepositoryTest
import com.example.chatdemo.R
import com.example.chatdemo.getOrAwaitValue
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Maybe
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class LoginViewModelTest : BaseChatRepositoryTest() {

    @MockK
    lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp() {
        every { firebaseUser.uid } returns "user id"
        every { firebaseUser.email } returns "email"
        every { repository.getLoggedInUser() } returns Maybe.empty()
    }


    @Test
    fun `verify successful login`() {
        every { repository.login(any(), any()) } returns Maybe.just(firebaseUser)

        val viewModel = LoginViewModel()
        viewModel.onLoginClicked("email", "password")

        verify { repository.login("email", "password") }
        assertThat(viewModel.userId.getOrAwaitValue()).isEqualTo("user id")
    }

    @Test
    fun `verify unsuccessful login`() {
        every { repository.login(any(), any()) } returns Maybe.error(Throwable("error"))

        val viewModel = LoginViewModel()
        viewModel.onLoginClicked("email", "password")

        assertThat(viewModel.error.getOrAwaitValue()).isEqualTo(
            Pair(
                R.string.login_failed,
                "error"
            )
        )
    }

    @Test
    fun `test already logged in user`() {
        every { repository.getLoggedInUser() } returns Maybe.just(firebaseUser)

        val viewModel = LoginViewModel()

        assertThat(viewModel.userId.getOrAwaitValue()).isEqualTo("user id")
    }
}
package com.example.chatdemo.room

import com.example.chatdemo.BaseChatRepositoryTest
import com.example.chatdemo.R
import com.example.chatdemo.getOrAwaitValue
import com.example.chatdemo.model.ChatMessage
import com.example.chatdemo.model.ChatRoom
import com.example.chatdemo.model.User
import io.mockk.every
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ChatRoomViewModelTest : BaseChatRepositoryTest() {
    private val user = User("user id", "nick name")
    private val chatRoom = ChatRoom("room id", "room name")
    private val messages = listOf(
        ChatMessage("sender 1", "message 1"),
        ChatMessage("sender 2", "message 2")
    )

    @Test
    fun `verify getting new messages`() {
        every { repository.getMessages(any()) } returns Flowable.just(messages)

        val viewModel = ChatRoomViewModel(user, chatRoom)

        verify { repository.getMessages(chatRoom) }
        assertThat(viewModel.messages.getOrAwaitValue()).isEqualTo(messages)
    }

    @Test
    fun `verify getting new messages failed`() {
        every { repository.getMessages(any()) } returns Flowable.error(Throwable("error"))

        val viewModel = ChatRoomViewModel(user, chatRoom)

        assertThat(viewModel.error.getOrAwaitValue()).isEqualTo(R.string.error)
    }

    @Test
    fun `verify sending new message`() {
        every { repository.getMessages(any()) } returns Flowable.just(messages)
        every { repository.sendMessage(any(), any(), any()) } returns Completable.complete()

        val viewModel = ChatRoomViewModel(user, chatRoom)
        viewModel.onSendMessage("new message")

        verify { repository.sendMessage(chatRoom, user, "new message") }
    }

    @Test
    fun `verify sending new message failed`() {
        every { repository.getMessages(any()) } returns Flowable.just(messages)
        every {
            repository.sendMessage(any(), any(), any())
        } returns Completable.error(Throwable("error"))

        val viewModel = ChatRoomViewModel(user, chatRoom)
        viewModel.onSendMessage("new message")

        assertThat(viewModel.error.getOrAwaitValue()).isEqualTo(R.string.error)
    }
}
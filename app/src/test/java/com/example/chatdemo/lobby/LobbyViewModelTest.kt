package com.example.chatdemo.lobby

import android.view.View
import com.example.chatdemo.BaseChatRepositoryTest
import com.example.chatdemo.R
import com.example.chatdemo.getOrAwaitValue
import com.example.chatdemo.model.ChatRoom
import com.example.chatdemo.model.User
import io.mockk.every
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

class LobbyViewModelTest : BaseChatRepositoryTest() {

    lateinit var viewModel: LobbyViewModel

    private val user = User("user id", "nick name")

    @Before
    fun setUp() {
        every { repository.getChatUser(any()) } returns Maybe.just(user)
        every { repository.getChatRooms() } returns Flowable.empty()
    }

    @Test
    fun `test Koin injection`() {
        viewModel = LobbyViewModel("user id")

        assertThat(viewModel.repository).isEqualTo(repository)
    }

    @Test
    fun `when new user, then nick name is asked and the user is added to the repository`() {
        every { repository.getChatUser(any()) } returns Maybe.empty()
        every { repository.createChatUser(any(), any()) } returns Single.just(user)

        viewModel = LobbyViewModel("user id")

        assertThat(viewModel.showChooseNickName.get()).isEqualTo(View.VISIBLE)

        verify { repository.getChatUser("user id") }

        viewModel.onCreateUser("nick name")

        verify { repository.createChatUser("user id", "nick name") }
        assertThat(viewModel.showChooseNickName.get()).isEqualTo(View.GONE)
        assertThat(viewModel.user.getOrAwaitValue()).isEqualTo(user)
    }

    @Test
    fun `when failed to fetch user, then an error is shown`() {
        every { repository.getChatUser(any()) } returns Maybe.error(Throwable("error"))
        every { repository.createChatUser(any(), any()) } returns Single.just(user)

        viewModel = LobbyViewModel("user id")

        assertThat(viewModel.error.getOrAwaitValue()).isEqualTo(R.string.error)
        assertThat(viewModel.showChooseNickName.get()).isEqualTo(View.GONE)
        assertThat(viewModel.showChatRooms.get()).isEqualTo(View.GONE)
    }

    @Test
    fun `given no rooms, when getting rooms, then an empty list of rooms is shown`() {
        val user = User("user id", "Nickname")
        val rooms = listOf(ChatRoom("room id", "room name"))
        every { repository.getChatRooms() } returns Flowable.empty()

        viewModel = LobbyViewModel("user id")

        assertThat(viewModel.showChooseNickName.get()).isEqualTo(View.GONE)
        assertThat(viewModel.showChatRooms.get()).isEqualTo(View.VISIBLE)
    }

    @Test
    fun `given there are existing rooms, when getting rooms, then list of rooms is shown`() {
        val rooms = listOf(
            ChatRoom("room id", "room name"),
            ChatRoom("room2 id", "room2 name")
        )
        every { repository.getChatRooms() } returns Flowable.just(rooms)

        viewModel = LobbyViewModel("user id")

        assertThat(viewModel.showChooseNickName.get()).isEqualTo(View.GONE)
        assertThat(viewModel.showChatRooms.get()).isEqualTo(View.VISIBLE)
        assertThat(viewModel.rooms.getOrAwaitValue()).isEqualTo(rooms)
        assertThat(viewModel.getRoomAt(0)).isEqualTo(rooms[0])
        assertThat(viewModel.getRoomAt(1)).isEqualTo(rooms[1])
    }

    @Test
    fun `when user creates new chat room, then it is added to the repository`() {
        val rooms = listOf(ChatRoom("room id", "room name"))
        every { repository.createChatRoom(any()) } returns Completable.complete()

        viewModel = LobbyViewModel("user id")
        viewModel.onCreateRoom("room name")

        verify { repository.createChatRoom("room name") }
    }

    @Test
    fun `when failed to create room, then an error is shown`() {
        every { repository.createChatRoom(any()) } returns Completable.error(Throwable("error"))

        viewModel = LobbyViewModel("user id")
        viewModel.onCreateRoom("room name")

        assertThat(viewModel.error.getOrAwaitValue()).isEqualTo(R.string.error)
    }

    @Test
    fun `when a room is selected from the list, then navigate to the chat room`() {
        val rooms = listOf(
            ChatRoom("room id", "room name"),
            ChatRoom("room2 id", "room2 name")
        )
        every { repository.getChatRooms() } returns Flowable.just(rooms)

        viewModel = LobbyViewModel("user id")

        viewModel.onItemClick(0)
        assertThat(viewModel.selectedRoom.getOrAwaitValue()).isEqualTo(Optional.of(rooms[0]))
        viewModel.onNavigationCompleted()
        assertThat(viewModel.selectedRoom.getOrAwaitValue()).isEqualTo(Optional.empty<ChatRoom>())

        viewModel.onItemClick(1)
        assertThat(viewModel.selectedRoom.getOrAwaitValue()).isEqualTo(Optional.of(rooms[1]))
        viewModel.onNavigationCompleted()
        assertThat(viewModel.selectedRoom.getOrAwaitValue()).isEqualTo(Optional.empty<ChatRoom>())
    }
}
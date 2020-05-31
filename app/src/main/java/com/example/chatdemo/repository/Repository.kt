package com.example.chatdemo.repository

import com.example.chatdemo.model.ChatMessage
import com.example.chatdemo.model.ChatRoom
import com.example.chatdemo.model.User
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface Repository {
    fun login(email: String, password: String): Maybe<FirebaseUser>
    fun createChatUser(userId: String, nickName: String): Single<User>
    fun getChatUser(userId: String): Maybe<User>
    fun createChatRoom(name: String): Completable
    fun getChatRooms(): Flowable<List<ChatRoom>>
    fun getMessages(room: ChatRoom): Flowable<List<ChatMessage>>
    fun sendMessage(room: ChatRoom, sender: User, message: String): Completable
}
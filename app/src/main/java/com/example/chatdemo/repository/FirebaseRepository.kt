package com.example.chatdemo.repository

import com.example.chatdemo.model.ChatMessage
import com.example.chatdemo.model.ChatRoom
import com.example.chatdemo.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import durdinapps.rxfirebase2.RxFirebaseAuth
import durdinapps.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single


private const val TAG = "FirebaseRepository"

private const val CHAT_USERS_COLLECTION = "users"
private const val CHAT_ROOMS_COLLECTION = "rooms"
private const val CHAT_MESSAGES_COLLECTION = "messages"

class FirebaseRepository : Repository {
    private val auth = Firebase.auth
    private val users = Firebase.firestore.collection(CHAT_USERS_COLLECTION)
    private val rooms = Firebase.firestore.collection(CHAT_ROOMS_COLLECTION)
    override fun login(
        email: String, password: String
    ): Maybe<FirebaseUser> {
        return RxFirebaseAuth.signInWithEmailAndPassword(auth, email, password)
            .map { authResult -> authResult.user }
    }

    override fun createChatUser(userId: String, nickName: String): Single<User> {
        val user = User(userId, nickName)
        return RxFirestore.setDocument(users.document(userId), user)
            .andThen(Single.just(user))
    }

    override fun getChatUser(userId: String): Maybe<User> {
        return RxFirestore.getDocument(
            users.document(userId),
            User::class.java
        )
    }

    override fun createChatRoom(name: String): Completable {
        return RxFirestore.addDocument(rooms, ChatRoom())
            .flatMapCompletable { ref ->
                RxFirestore.setDocument(ref, ChatRoom(ref.id, name))
            }
    }

    override fun getChatRooms(): Flowable<List<ChatRoom>> {
        return RxFirestore.observeQueryRef(rooms, ChatRoom::class.java)
            .map { rooms -> rooms.sortedBy { it.name } }
    }

    override fun getMessages(room: ChatRoom): Flowable<List<ChatMessage>> {
        val messagesRef = rooms.document(room.id).collection(CHAT_MESSAGES_COLLECTION)
        return RxFirestore.observeQueryRef(messagesRef, ChatMessage::class.java)
            .map { messages -> messages.sortedBy { it.timeStamp } }
    }

    override fun sendMessage(
        room: ChatRoom, sender: User, message: String
    ): Completable {
        val messagesRef = rooms.document(room.id).collection(CHAT_MESSAGES_COLLECTION)
        return RxFirestore.addDocument(messagesRef, ChatMessage())
            .flatMapCompletable { ref ->
                RxFirestore.setDocument(ref, ChatMessage(sender.nickName, message))
            }
    }
}
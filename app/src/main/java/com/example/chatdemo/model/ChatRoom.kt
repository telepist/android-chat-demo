package com.example.chatdemo.model

import java.io.Serializable

data class ChatRoom(
    val id: String = "",
    val name: String = ""
) : Serializable
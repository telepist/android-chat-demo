package com.example.chatdemo.model

import java.io.Serializable

data class User(
    val userId: String = "",
    val nickName: String = ""
) : Serializable
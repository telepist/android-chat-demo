package com.example.chatdemo.model

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

data class ChatMessage(
    val sender: String = "",
    val message: String = "",
    val timeStamp: Long = System.currentTimeMillis()
) {
    fun getTimeStampFormatted(): String {
        val instant = Instant.ofEpochMilli(timeStamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
    }
}
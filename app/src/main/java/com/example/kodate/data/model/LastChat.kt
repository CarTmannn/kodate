package com.example.kodate.data.model

import com.google.firebase.Timestamp

data class LastChat(
    val chatId: String = "",
    val users: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageAt: Timestamp? = null,
    val read: Map<String, Boolean> = emptyMap()
)

data class Message(
    val senderId: String = "",
    val message: String = "",
    val setAt: Timestamp? = null,
    val isRead: Boolean = false
)


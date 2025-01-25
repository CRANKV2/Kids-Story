package com.gigo.storyflow.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val isUser: Boolean,
    val timestamp: Long
)

// Extension functions für die Konvertierung
fun ChatMessage.toEntity() = ChatMessageEntity(
    content = content,
    isUser = isUser,
    timestamp = timestamp
)

fun ChatMessageEntity.toMessage() = ChatMessage(
    content = content,
    isUser = isUser,
    timestamp = timestamp
) 
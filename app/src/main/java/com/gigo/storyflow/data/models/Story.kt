package com.gigo.storyflow.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class Story(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val imagePath: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) 
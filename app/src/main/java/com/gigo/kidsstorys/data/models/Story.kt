package com.gigo.kidsstorys.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class Story(
    @PrimaryKey val id: Int = 0,
    var title: String,
    var content: String,
    var imagePath: String? = null,
    var timestamp: Long = System.currentTimeMillis()
) 
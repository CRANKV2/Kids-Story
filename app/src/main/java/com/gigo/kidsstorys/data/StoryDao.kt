package com.gigo.testapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories ORDER BY id DESC")
    fun getAllStories(): Flow<List<Story>>

    @Query("SELECT * FROM stories WHERE id = :id")
    fun getStoryById(id: Int): Flow<Story?>

    @Insert
    suspend fun insertStory(story: Story)

    @Update
    suspend fun updateStory(story: Story)

    @Delete
    suspend fun deleteStory(story: Story)
} 
package com.gigo.testapp.data

import kotlinx.coroutines.flow.Flow

class StoryRepository(private val storyDao: StoryDao) {
    val allStories: Flow<List<Story>> = storyDao.getAllStories()

    suspend fun insertStory(story: Story) {
        storyDao.insertStory(story)
    }

    suspend fun updateStory(story: Story) {
        storyDao.updateStory(story)
    }

    suspend fun deleteStory(story: Story) {
        storyDao.deleteStory(story)
    }
} 
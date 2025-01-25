package com.gigo.storyflow.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gigo.storyflow.StoryFlowApp
import com.gigo.storyflow.data.UserPreferences
import com.gigo.storyflow.data.UserPreferencesRepository
import com.gigo.storyflow.data.dao.StoryDao
import com.gigo.storyflow.data.models.Story
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StoryViewModel(
    private val storyDao: StoryDao,
    private val userPreferencesRepository: UserPreferencesRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _selectedStory = MutableStateFlow<Story?>(null)
    val selectedStory = _selectedStory.asStateFlow()

    private val _allStories = MutableStateFlow<List<Story>>(emptyList())
    val allStories = _allStories.asStateFlow()

    val userPreferences = userPreferencesRepository.userPreferencesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserPreferences()
    )

    init {
        viewModelScope.launch {
            storyDao.getAllStories().collect { stories ->
                _allStories.value = stories
            }
        }
    }

    fun loadStory(id: Int) {
        viewModelScope.launch {
            storyDao.getStoryById(id).collect { story ->
                _selectedStory.value = story
            }
        }
    }

    fun addStory(title: String, content: String, imagePath: String? = null) {
        viewModelScope.launch {
            val story = Story(
                title = title,
                content = content,
                imagePath = imagePath
            )
            storyDao.insertStory(story)
        }
    }

    fun deleteStory(story: Story) {
        viewModelScope.launch {
            storyDao.deleteStory(story)
        }
    }

    fun updateStory(story: Story) {
        viewModelScope.launch {
            storyDao.updateStory(story)
            loadStory(story.id)
        }
    }

    fun updateCardTitleColor(color: Color) {
        viewModelScope.launch {
            userPreferencesRepository.updateCardTitleColor(color)
        }
    }

    fun updateCardPreviewColor(color: Color) {
        viewModelScope.launch {
            userPreferencesRepository.updateCardPreviewColor(color)
        }
    }

    fun updateStoryTitleColor(color: Color) {
        viewModelScope.launch {
            userPreferencesRepository.updateStoryTitleColor(color)
        }
    }

    fun updateStoryTextColor(color: Color) {
        viewModelScope.launch {
            userPreferencesRepository.updateStoryTextColor(color)
        }
    }


    fun updateStoryImage(storyId: Int, imagePath: String?) {
        viewModelScope.launch {
            try {
                val story = storyDao.getStoryById(storyId).first()
                story?.let {
                    val updatedStory = it.copy(imagePath = imagePath)
                    storyDao.updateStory(updatedStory)
                }
            } catch (e: Exception) {
                Log.e("StoryViewModel", "Error updating story image", e)
            }
        }
    }


    fun deleteStories(storyIds: List<Int>) {
        viewModelScope.launch {
            storyIds.forEach { id ->
                // Sammle den Flow und hole die Story
                storyDao.getStoryById(id).collect { story ->
                    story?.let {
                        deleteStory(it)
                    }
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = StoryFlowApp.getInstance()
                StoryViewModel(
                    storyDao = application.database.storyDao(),
                    userPreferencesRepository = application.userPreferencesRepository,
                    application = application
                )
            }
        }
    }
} 
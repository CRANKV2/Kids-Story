package com.gigo.kidsstorys.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gigo.kidsstorys.MyApplication
import com.gigo.kidsstorys.data.Story
import com.gigo.kidsstorys.data.StoryDao
import com.gigo.kidsstorys.data.UserPreferences
import com.gigo.kidsstorys.data.UserPreferencesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StoryViewModel(
    private val storyDao: StoryDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

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

    fun addStory(title: String, content: String) {
        viewModelScope.launch {
            val story = Story(title = title, content = content)
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

    fun updateStoryTitle(story: Story, newTitle: String) {
        viewModelScope.launch {
            val updatedStory = story.copy(title = newTitle)
            storyDao.updateStory(updatedStory)
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

    fun updateFontSize(newSize: Float) {
        viewModelScope.launch {
            userPreferencesRepository.updateFontSize(newSize)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val application = MyApplication.instance
                return StoryViewModel(
                    application.database.storyDao(),
                    application.userPreferencesRepository
                ) as T
            }
        }
    }
} 
package com.gigo.storyflow.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigo.storyflow.BuildConfig
import com.gigo.storyflow.StoryFlowApp
import com.gigo.storyflow.data.dao.ChatMessageDao
import com.gigo.storyflow.data.models.ChatMessage
import com.gigo.storyflow.data.models.toEntity
import com.gigo.storyflow.data.models.toMessage
import com.gigo.storyflow.utils.PromptUtils
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatMessageDao: ChatMessageDao = StoryFlowApp.getInstance().database.chatMessageDao()
) : ViewModel() {
    val messages = chatMessageDao.getAllMessages()
        .map { entities -> entities.map { it.toMessage() } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 1f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        }
    )
    
    private var chat = model.startChat(emptyList())

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Prüfe ob es eine Story-Anfrage ist
                val isStoryRequest = message.lowercase().contains("geschichte") || 
                                    message.lowercase().contains("erzähl") ||
                                    message.lowercase().contains("story")
                
                // Speichere User-Nachricht
                val userMessage = ChatMessage(message, true)
                chatMessageDao.insertMessage(userMessage.toEntity())
                
                if (isStoryRequest) {
                    // Erst Titel generieren
                    val titlePrompt = PromptUtils.createTitlePrompt(message)
                    val titleResponse = chat.sendMessage(titlePrompt)
                    val titleText = titleResponse.text ?: "Titel konnte nicht generiert werden."
                    
                    // Titel als separate Nachricht speichern
                    val titleMessage = ChatMessage(titleText, false)
                    chatMessageDao.insertMessage(titleMessage.toEntity())
                    
                    // Dann die Geschichte generieren
                    val storyPrompt = PromptUtils.createStoryPrompt(thema = message)
                    val storyResponse = chat.sendMessage(storyPrompt)
                    val storyText = storyResponse.text ?: "Entschuldigung, ich konnte keine Geschichte generieren."
                    
                    // Geschichte als separate Nachricht speichern
                    val storyMessage = ChatMessage(storyText, false)
                    chatMessageDao.insertMessage(storyMessage.toEntity())
                } else {
                    // Normaler Konversations-Flow
                    val prompt = PromptUtils.createConversationPrompt(message)
                    val response = chat.sendMessage(prompt)
                    val responseText = response.text ?: "Entschuldigung, ich konnte keine Antwort generieren."
                    
                    val aiMessage = ChatMessage(responseText, false)
                    chatMessageDao.insertMessage(aiMessage.toEntity())
                }
                
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearChat() {
        viewModelScope.launch {
            chatMessageDao.deleteAllMessages()
            chat = model.startChat(emptyList())
            _error.value = null
        }
    }

}
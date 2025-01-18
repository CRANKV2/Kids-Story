package com.gigo.kidsstorys.ui.viewmodels

import com.gigo.kidsstorys.utils.PromptUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigo.kidsstorys.BuildConfig
import com.gigo.kidsstorys.KidsStorysApp
import com.gigo.kidsstorys.data.dao.ChatMessageDao
import com.gigo.kidsstorys.data.models.ChatMessage
import com.gigo.kidsstorys.data.models.toEntity
import com.gigo.kidsstorys.data.models.toMessage
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
    private val chatMessageDao: ChatMessageDao = KidsStorysApp.getInstance().database.chatMessageDao()
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
                
                // Erstelle den passenden Prompt
                val prompt = if (isStoryRequest) {
                    PromptUtils.createStoryPrompt(thema = message)
                } else {
                    PromptUtils.createConversationPrompt(message)
                }
                
                // Speichere User-Nachricht
                val userMessage = ChatMessage(message, true)
                chatMessageDao.insertMessage(userMessage.toEntity())
                
                // Sende Prompt an Gemini
                val response = chat.sendMessage(prompt)
                val responseText = response.text ?: "Entschuldigung, ich konnte keine Antwort generieren."
                
                // Speichere AI-Antwort
                val aiMessage = ChatMessage(responseText, false)
                chatMessageDao.insertMessage(aiMessage.toEntity())
                
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
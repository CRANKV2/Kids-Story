package com.gigo.kidsstorys.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.gigo.kidsstorys.BuildConfig
import com.gigo.kidsstorys.data.models.ChatMessage
import com.gigo.kidsstorys.data.dao.ChatMessageDao
import com.gigo.kidsstorys.data.models.toEntity
import com.gigo.kidsstorys.data.models.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import com.gigo.kidsstorys.KidsStorysApp
import kotlinx.coroutines.TimeoutCancellationException
import java.io.IOException
import kotlinx.coroutines.withTimeout

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
        if (message.isBlank()) {
            _error.value = "Die Nachricht darf nicht leer sein"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // Benutzer-Nachricht speichern
            chatMessageDao.insertMessage(ChatMessage(message, true).toEntity())
            
            try {
                val response = withTimeout(30000) { // 30 Sekunden Timeout
                    chat.sendMessage(message)
                }
                
                val botResponse = response.text ?: "Entschuldigung, ich konnte keine passende Antwort generieren"
                chatMessageDao.insertMessage(ChatMessage(botResponse, false).toEntity())
                
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is TimeoutCancellationException -> 
                        "Die Anfrage hat zu lange gedauert. Bitte versuche es erneut."
                    is IOException -> 
                        "Keine Internetverbindung. Bitte überprüfe deine Verbindung."
                    else -> 
                        "Ein Fehler ist aufgetreten: ${e.message}"
                }
                
                _error.value = errorMessage
                chatMessageDao.insertMessage(
                    ChatMessage(errorMessage, false).toEntity()
                )
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

    fun clearError() {
        _error.value = null
    }
} 
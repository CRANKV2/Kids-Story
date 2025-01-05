package com.gigo.kidsstorys.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.gigo.kidsstorys.BuildConfig

class ChatViewModel : ViewModel() {
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
    
    suspend fun sendMessage(message: String): String {
        return try {
            val response = chat.sendMessage(message)
            response.text ?: "Keine Antwort erhalten"
        } catch (e: Exception) {
            "Fehler: ${e.message}"
        }
    }
} 
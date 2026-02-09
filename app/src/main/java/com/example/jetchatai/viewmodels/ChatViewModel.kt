package com.example.jetchatai.viewmodels

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetchatai.BuildConfig
import com.example.jetchatai.data.GroqService
import com.example.jetchatai.viewmodels.MessageModel
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val groqService = GroqService(BuildConfig.API_KEY)
    val messageList = mutableStateListOf<MessageModel>()
    var isLoading by mutableStateOf(false)

    private val localKnowledge: String by lazy {
        application.assets.open("ysm_knowledge.txt").bufferedReader().use { it.readText() }
    }

    fun sendMessage(userInput: String) {
        if (userInput.isBlank()) return

        val userMessage = MessageModel(userInput, "user")
        messageList.add(userMessage)
        isLoading = true

        viewModelScope.launch {
            try {
                val systemInstructions = """
                    You are 'JetChat', the official AI assistant for Ysmayyl Mammetgeldiyev.
                    Ysmayyl is a CS student at ELTE with experience in Cybersecurity and Linux.
                    
                    KNOWLEDGE BASE:
                    $localKnowledge
                    
                    RULES:
                    - Use the provided knowledge to answer questions about Ysmayyl.
                    - If a fact is missing, give his email: smiletechweb@gmail.com.
                    - Be witty and end with a 'Pro-tip' 🚀.
                """.trimIndent()

                val apiMessages = mutableListOf<MessageModel>()

                apiMessages.add(MessageModel(systemInstructions, "user"))
                apiMessages.add(MessageModel("Understood. I am JetChat and I will represent Ysmayyl.", "model"))

                apiMessages.addAll(messageList)

                val result = groqService.getResponse(apiMessages)
                messageList.add(MessageModel(result, "model"))

            } catch (e: Exception) {
                messageList.add(MessageModel("Error connecting to Groq API: ${e.message}", "model"))
            } finally {
                isLoading = false
            }
        }
    }
}
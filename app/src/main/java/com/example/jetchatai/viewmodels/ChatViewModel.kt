package com.example.jetchatai.viewmodels

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetchatai.BuildConfig
import com.example.jetchatai.data.GroqService
import com.example.jetchatai.shared.models.MessageModel
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val groqService = GroqService(BuildConfig.API_KEY)

    val messageList = mutableStateListOf<MessageModel>()
    var isLoading by mutableStateOf(false)


    private val localKnowledge: String by lazy {
        try {
            application.assets.open("ysm_knowledge.txt").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            "No local knowledge found."
        }
    }

    fun sendMessage(userInput: String) {
        if (userInput.isBlank()) return

        val userMessage = MessageModel(
            text = userInput,
            role = "user",
            timestamp = System.currentTimeMillis()
        )

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

                apiMessages.add(
                    MessageModel(
                        text = systemInstructions,
                        role = "user",
                        timestamp = System.currentTimeMillis()
                    )
                )
                apiMessages.add(
                    MessageModel(
                        text = "Understood. I am JetChat and I will represent Ysmayyl.",
                        role = "model",
                        timestamp = System.currentTimeMillis()
                    )
                )

                // Add previous chat history
                apiMessages.addAll(messageList)

                val result = groqService.getResponse(apiMessages)

                // 3. Fix: Add timestamp for the AI response
                messageList.add(
                    MessageModel(
                        text = result,
                        role = "model",
                        timestamp = System.currentTimeMillis()
                    )
                )

            } catch (e: Exception) {
                messageList.add(
                    MessageModel(
                        text = "Error connecting to Groq API: ${e.message}",
                        role = "model",
                        timestamp = System.currentTimeMillis()
                    )
                )
            } finally {
                isLoading = false
            }
        }
    }
}
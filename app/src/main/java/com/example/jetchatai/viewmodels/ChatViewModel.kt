package com.example.jetchatai.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetchatai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val generativeModel = GenerativeModel(
        // Ensure this is a standard hyphen: gemini-1.5-flash
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.API_KEY
    )

    private val chatSession = generativeModel.startChat()

    val messageList = mutableStateListOf<MessageModel>()

    fun sendMessage(userInput: String){
        if (userInput.isBlank()) return

        messageList.add(MessageModel(userInput, "user"))

        viewModelScope.launch {
            try {
                // The SDK uses the chatSession to maintain context
                val response = chatSession.sendMessage(userInput)

                response.text?.let { resultText ->
                    messageList.add(MessageModel(resultText, "model"))
                }
            } catch (e: Exception) {
                // This catches network issues or API limit errors
                messageList.add(MessageModel("Sorry, I encountered an error: ${e.localizedMessage}", "model"))
            }
        }
    }
}
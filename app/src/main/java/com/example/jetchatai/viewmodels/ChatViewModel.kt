package com.example.jetchatai.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetchatai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.API_KEY  //Paste there your API_KEY from google
    )

    private val chatSession = generativeModel.startChat()

    val messageList = mutableStateListOf<MessageModel>()
    var isLoading by mutableStateOf(false)
    fun sendMessage(userInput: String){
        if (userInput.isBlank()) return
        isLoading = true
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
            }finally {
                isLoading = false // Stop thinking
            }
        }
    }
}
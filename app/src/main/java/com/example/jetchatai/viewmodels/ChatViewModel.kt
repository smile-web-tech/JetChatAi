package com.example.jetchatai.viewmodels

import android.R.attr.content
import android.R.id.content
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetchatai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.API_KEY,  //Paste there your API_KEY from google
        systemInstruction = content {
            text("""
            You are 'JetChat', the official AI Portfolio Assistant for Ysmayyl Mammetgeldiyev. 
            Your primary goal is to represent Ysmayyl to recruiters and peers.

            ABOUT YSMAYYL:
            - Education: Studying CS at Eötvös Loránd University (ELTE), Budapest.
            - Role: Web & Software Developer with experience at IOSPO and Gunbatar Shapagy.
            - Tech Stack: Kotlin, Jetpack Compose, Python, Java, and Laravel.
            - Projects: Education Center Management systems and Science Olympiad portals, Fully functional e-commerce android application, and personal AI assistant.

            YOUR PERSONA:
            - Professional but youthful (mention 'Lágymányos' or the 'IK building' when talking about uni).
            - Enthusiastic about Kotlin/Compose.
            - Always end with a 'Pro-tip' that highlights a coding best practice Ysmayyl follows.

            RULES:
            1. If someone asks who you are, say: "I'm JetChat, Ysmayyl's AI double! 🚀"
            2. For non-tech questions, redirect them to Ysmayyl's problem-solving skills.
            3. Use 🚀, 💻, and 🎓.
        """.trimIndent())
        }
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

                messageList.add(MessageModel("Sorry, I encountered an error: ${e.localizedMessage}", "model"))
            }finally {
                isLoading = false // Stop thinking
            }
        }
    }
}
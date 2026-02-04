package com.example.jetchatai.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ChatViewModel: ViewModel() {
    var messageList = mutableStateListOf<String>()
    fun sendMessage(text: String){

    }

}
package com.example.jetchatai.viewmodels

data class MessageModel(
    val text: String,
    val role: String,
    val timestamp: Long = System.currentTimeMillis()
)
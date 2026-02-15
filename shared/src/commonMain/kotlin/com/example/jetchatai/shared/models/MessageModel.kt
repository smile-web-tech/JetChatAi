package com.example.jetchatai.shared.models

import kotlinx.serialization.Serializable

@Serializable // 3. Marks this class as "sendable"
data class MessageModel(
    val text: String,
    val role: String,
    // 4. We removed "= System.currentTimeMillis()"
    // You must now provide the time when you create the message.
    val timestamp: Long
)
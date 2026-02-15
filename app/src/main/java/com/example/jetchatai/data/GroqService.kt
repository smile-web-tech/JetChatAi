package com.example.jetchatai.data

import com.example.jetchatai.shared.models.MessageModel
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.json.JSONObject
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class GroqService(private val apiKey: String) {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }


    suspend fun getResponse(messages: List<MessageModel>): String {
        val requestBody = buildJsonObject {
            put("model", "llama-3.1-8b-instant")
            put("messages", buildJsonArray {
                messages.forEach { msg ->
                    addJsonObject {
                        put("role", when(msg.role) {
                            "user" -> "user"
                            "assistant", "model" -> "assistant"
                            else -> "user"
                        })
                        put("content", msg.text)
                    }
                }
            })
        }

        val response: HttpResponse = client.post("https://api.groq.com/openai/v1/chat/completions") {
            header(HttpHeaders.Authorization, "Bearer $apiKey")
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        val body = response.bodyAsText()
        val json = JSONObject(body)
        
        if (json.has("error")) {
            val error = json.getJSONObject("error")
            return "API Error: ${error.optString("message", "Unknown error")}"
        }
        
        if (!json.has("choices")) {
            return "Error: Unexpected API response format. Body: $body"
        }
        
        return json.getJSONArray("choices")
            .getJSONObject(0).getJSONObject("message").getString("content")
    }
}
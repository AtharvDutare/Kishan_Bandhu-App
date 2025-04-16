package com.example.trysample.mydata

data class ConversationResponse(
    val messages: List<Message>,
    val identification: String,
    val remaining_calls: Int,
    val model_parameters: ModelParameters,
    val feedback: Map<String, Any> // You can type this better if you know the structure
)
data class Message(
    val content: String,
    val type: String,
    val created: String
)
data class ModelParameters(
    val model: String,
    val temperature: Double
)
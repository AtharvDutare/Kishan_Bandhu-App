package com.example.trysample.mydata

data class ConversationRequest(
val question: String,
val prompt: String,
val temperature: Double,
val app_name: String
)
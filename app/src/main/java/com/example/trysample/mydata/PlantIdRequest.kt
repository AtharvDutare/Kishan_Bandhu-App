package com.example.trysample.mydata



data class PlantIdRequest(
    val images: List<String>,
    val latitude: Double,
    val longitude: Double,
    val similar_images: Boolean
)
package com.example.trysample.mydata

data class PlantIdResponse(
    val access_token: String,
    val model_version: String,
    val custom_id: String?,
    val input: Input,
    val result: Result,
    val status: String,
    val sla_compliant_client: Boolean,
    val sla_compliant_system: Boolean,
    val created: Double,
    val completed: Double
)

data class Input(
    val latitude: Double,
    val longitude: Double,
    val similar_images: Boolean,
    val images: List<String>,
    val datetime: String
)

data class Result(
    val is_plant: IsPlant,
    val classification: Classification
)

data class IsPlant(
    val probability: Double,
    val binary: Boolean,
    val threshold: Double
)

data class Classification(
    val suggestions: List<Suggestion>
)

data class Suggestion(
    val id: String,
    val name: String,
    val probability: Double,
    val similar_images: List<SimilarImage>,
    val details: Details
)

data class SimilarImage(
    val id: String,
    val url: String,
    val license_name: String?,
    val license_url: String?,
    val citation: String?,
    val similarity: Double,
    val url_small: String
)

data class Details(
    val language: String,
    val entity_id: String
)
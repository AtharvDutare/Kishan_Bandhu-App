package com.example.trysample.mydata


data class PlantHealthResponse(
    val access_token: String,
    val model_version: String,
    val custom_id: String? = null,
    val input: HealthInput,
    val result: HealthResult,
    val status: String,
    val sla_compliant_client: Boolean,
    val sla_compliant_system: Boolean,
    val created: Double,
    val completed: Double
)

data class HealthInput(
    val latitude: Double,
    val longitude: Double,
    val similar_images: Boolean,
    val health: String,
    val images: List<String>,
    val datetime: String
)

data class HealthResult(
    val is_plant: IsPlantHealthy,
    val is_healthy: IsHealthy,
    val disease: Disease
)

data class IsPlantHealthy(
    val probability: Double,
    val threshold: Double,
    val binary: Boolean
)

data class IsHealthy(
    val binary: Boolean,
    val threshold: Double,
    val probability: Double
)

data class Disease(
    val suggestions: List<HealthSuggestion>
)

data class HealthSuggestion(
    val id: String,
    val name: String,
    val probability: Double,
    val similar_images: List<SimilarImageHealth>,
    val details: HealthDetails
)

data class SimilarImageHealth(
    val id: String,
    val url: String,
    val license_name: String,
    val license_url: String,
    val citation: String,
    val similarity: Double,
    val url_small: String
)

data class HealthDetails(
    val language: String,
    val entity_id: String
)
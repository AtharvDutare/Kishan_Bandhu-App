package com.example.trysample.plantretrofit

import com.example.trysample.mydata.ConversationRequest
import com.example.trysample.mydata.ConversationResponse
import com.example.trysample.mydata.PlantHealthResponse
import com.example.trysample.mydata.PlantIdRequest
import com.example.trysample.mydata.PlantIdResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PlantInterface {


    @Headers(
        "Content-Type: application/json",
        "Api-Key: yvuAOrVVgF1flex4PUV8C0R4iEo1ztSPJGobQK3uW3c3oyytlT"
    )
    @POST("identification")

    fun identifyPlant(
        @Body request : PlantIdRequest
    ) : Call<PlantIdResponse>


    @Headers(
        "Content-Type: application/json",
        "Api-Key: yvuAOrVVgF1flex4PUV8C0R4iEo1ztSPJGobQK3uW3c3oyytlT"
    )
    @POST("health_assessment")

    fun getPlantHealth(
        @Body request : PlantIdRequest
    ) : Call<PlantHealthResponse>


    @Headers(
        "Content-Type: application/json",
        "Api-Key: yvuAOrVVgF1flex4PUV8C0R4iEo1ztSPJGobQK3uW3c3oyytlT"
    )
    @POST("identification/{identification_id}/conversation")
    fun startConversation(
        @Path("identification_id") identificationId: String,
        @Body request : ConversationRequest
    ) : Call<ConversationResponse>

}
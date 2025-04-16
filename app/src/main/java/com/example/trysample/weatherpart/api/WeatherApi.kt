package com.example.trysample.weatherpart.api

import com.example.trysample.mydata.ConversationRequest
import com.example.trysample.mydata.ConversationResponse
import com.example.trysample.mydata.PlantHealthResponse
import com.example.trysample.mydata.PlantIdRequest
import com.example.trysample.mydata.PlantIdResponse
import com.example.trysample.weatherpart.datamodel.WeatherModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v1/current.json")
    suspend fun getWeather(
        @Query("key") apikey: String,
        @Query("q") city:String
    ):Response<WeatherModel>

    // Below is scan related functions




}
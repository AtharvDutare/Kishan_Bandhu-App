package com.example.trysample.plantretrofit

import com.example.trysample.weatherpart.api.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstancePlant {

    private val BASE_URL="https://plant.id/api/v3/"

    private fun getInstance(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val plantInterface: PlantInterface = getInstance().create(PlantInterface::class.java)


}
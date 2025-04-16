package com.example.trysample.weatherpart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trysample.networkresponse.NetworkResponse
import com.example.trysample.weatherpart.api.Constant
import com.example.trysample.weatherpart.api.RetrofitInstanceWeather
import com.example.trysample.weatherpart.datamodel.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val weatherApi= RetrofitInstanceWeather.weatherApi
    private val _weatherResult= MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResults: LiveData<NetworkResponse<WeatherModel>> =_weatherResult

    fun getLocation(city:String) {
        _weatherResult.value= NetworkResponse.Loading
        viewModelScope.launch {
           try {
               val response= weatherApi.getWeather(Constant.weatherApiKey,city)
               if(response.isSuccessful) {
                   //Log.i("Response",response.body().toString())
                   response.body()?.let {
                       _weatherResult.value= NetworkResponse.Success(it)
                   }
               }
               else {
                   //Log.i("error","nahi aaya")
                   _weatherResult.value= NetworkResponse.Error("Failed to Load Data")
               }
           }
           catch (e: Exception) {
               _weatherResult.value= NetworkResponse.Error("Failed to Load Data")
           }
        }

    }


}
package com.example.trysample.myviewmodels
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trysample.plantretrofit.RetrofitInstancePlant
import com.example.trysample.mydata.ConversationRequest
import com.example.trysample.mydata.ConversationResponse
import com.example.trysample.mydata.Disease
import com.example.trysample.mydata.Message
import com.example.trysample.mydata.PlantHealthResponse
import com.example.trysample.mydata.PlantIdRequest
import com.example.trysample.mydata.PlantIdResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanCropViewModel : ViewModel() {




    private val _diseasesInformation = MutableStateFlow<Disease?>(null)
    val diseaseInformation : StateFlow<Disease?>get() = _diseasesInformation.asStateFlow()

    private val _isBotThinking = MutableStateFlow<Boolean> (false)
    val isBotThinking : StateFlow<Boolean> get() = _isBotThinking.asStateFlow()

    private val _isAnalyzing = MutableStateFlow<Boolean> (false)
    val isAnalyzing : StateFlow<Boolean> get() = _isAnalyzing.asStateFlow()

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken : StateFlow<String?> get() = _accessToken.asStateFlow()

    private val _plantHealthAllInformation = MutableStateFlow<PlantHealthResponse?>(null)
    val plantHealthAllInformation : StateFlow<PlantHealthResponse?>get () = _plantHealthAllInformation.asStateFlow()

    private val _chatHistory = MutableStateFlow<List<Message>>(emptyList())
    val chatHistory : StateFlow<List<Message>>get() = _chatHistory.asStateFlow()



    fun fetchConversation(question : String ) {
        Log.d("Bot","Going to fetch conversation")
        viewModelScope.launch {
            _isBotThinking.value = true
            val request = ConversationRequest(
                question = question,
                prompt = "Give answer",
                temperature = 0.5,
                app_name = "MyAppBot"
            )

            _accessToken.collectLatest {

                if(it != null){

                    Log.d("ShowChats","Fetching data for crop Access Token is $it <-")
                    val call = RetrofitInstancePlant.plantInterface.startConversation(it , request)

                    call.enqueue(object : Callback<ConversationResponse> {
                        override fun onResponse(
                            call: Call<ConversationResponse>,
                            response: Response<ConversationResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("Access_Token","Fetch Conversation is success")
                                val messages = response.body()?.messages
                                messages?.forEach {
                                    Log.d("Bot", "${it.type.uppercase()}: ${it.content}")
                                }

                                if(messages != null){
                                    _isBotThinking.value = false
                                    Log.d("Bot","Chathistory updated")
                                    _chatHistory.value = messages

                                    Log.d("ShowChats","Chat History size is ${_chatHistory.value.size}")
                                }
                            } else {
                                _isBotThinking.value = false
                                Log.d("Access_Token","Fetch Conversation is not  success")
                                Log.e("Bot", "Error: ${response.body()}")
                            }
                        }

                        override fun onFailure(call: Call<ConversationResponse>, t: Throwable) {
                            _isBotThinking.value  = false
                            Log.e("Bot", "Failure: ${t.message}")
                        }
                    })
                }
                else {
                    Log.d("Access_Token","it is null ")

                }

            }

        }

    }



    fun fetchIdentification(userSelectedImageBase64: String) {
        _isBotThinking.value  = true
        Log.d("UserSelectedImageBase64", userSelectedImageBase64.take(100))
        val request = PlantIdRequest(
            images = listOf(userSelectedImageBase64),
            latitude = 49.207,
            longitude = 16.608,
            similar_images = true
        )

        RetrofitInstancePlant.plantInterface.identifyPlant(request)
            .enqueue(object : Callback<PlantIdResponse> {
                override fun onResponse(
                    call: Call<PlantIdResponse>,
                    response: Response<PlantIdResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        Log.d("UserSelectedImageBase64","Status against user image is ${data?.status}")
                        Log.d("Access_Token","${data?.access_token}")
                        Log.d("UserSelectedImageBase64","${data?.result?.is_plant?.probability}")
                        _accessToken.value = data?.access_token
                    } else {
                        Log.d("Access_Token","Response is not success in plant identification")
                        _isBotThinking.value = false
                        Log.e("Access_Token", "Error: ${response.message()}")
                    }
                }


                override fun onFailure(call: Call<PlantIdResponse>, t: Throwable) {
                    _isAnalyzing.value = false
                    Log.e("Access_Token", "Failure: ${t.localizedMessage}")
                }
            })
    }




    fun fetchPlantHealth(base64ImageInput : String ){
        Log.d("PlantHealth","Going to fetch plant health")
        viewModelScope.launch {
            _isAnalyzing.value = true
            val request = PlantIdRequest(
                images = listOf(base64ImageInput),
                latitude = 49.207,
                longitude = 16.608,
                similar_images = true
            )

            RetrofitInstancePlant.plantInterface.getPlantHealth(request)
                .enqueue(object : Callback<PlantHealthResponse> {
                    override fun onResponse(
                        call: Call<PlantHealthResponse>,
                        response: retrofit2.Response<PlantHealthResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()

                            _plantHealthAllInformation.value = data
                            _diseasesInformation.value = data?.result?.disease
                            _isAnalyzing.value = false

                            Log.d("PlantHealth","${data?.status}")
                        } else {
                            _isAnalyzing.value = false
                            Log.e("PlantHealth", "Error: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<PlantHealthResponse>, t: Throwable) {
                        _isAnalyzing.value = false
                        Log.e("PlantHealth", "Failure: ${t.localizedMessage}")
                    }
                })

        }
    }
}
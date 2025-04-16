package com.example.trysample.myviewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trysample.auth.AuthService
import com.example.trysample.auth.AuthState
import com.example.trysample.mydata.AgentPost
import com.example.trysample.mydata.MarketPost
import com.example.trysample.myrepository.HomeScreenRepo
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {



    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()
    private val firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseStorage : FirebaseStorage = FirebaseStorage.getInstance()


    private val authService = AuthService()

    private val _isMarketPostLoading = MutableStateFlow<Boolean>(false)
    val isMarketPostLoading : StateFlow<Boolean>get() = _isMarketPostLoading.asStateFlow()


    // Authentication state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _listOfMarketPosts = MutableStateFlow<List<MarketPost>>(emptyList())
    val listOfMarketPosts : StateFlow<List<MarketPost>>get() = _listOfMarketPosts.asStateFlow()


    private val _listOfAgentPost = MutableStateFlow<List<AgentPost>>(emptyList())
    val listOfAgentPost : StateFlow<List<AgentPost>>get() = _listOfAgentPost.asStateFlow()

    val homeScreenRepo : HomeScreenRepo = HomeScreenRepo()


    private val _isUploading = MutableStateFlow<Boolean>(false)
    val isUploading : StateFlow<Boolean> get() = _isUploading.asStateFlow()


    init {
        _currentUser.value = authService.getCurrentUser()
        if (_currentUser.value != null) {
            _authState.value = AuthState.SignedIn(_currentUser.value!!)
        }

        viewModelScope.launch {
            authService.observeAuthStateChanges().collect { user ->
                _currentUser.value = user
                _authState.value = if (user != null) {
                    AuthState.SignedIn(user)
                } else {
                    AuthState.SignedOut
                }
            }
        }

        fetchAllMarketPosts()

        fetchAllAgentPost()
    }


    fun addMarketPlacePost(marketPost: MarketPost, onPostAdded : () -> Unit ){
        viewModelScope.launch {

            _isMarketPostLoading.value  = true

            homeScreenRepo.addMarketPlacePost(firebaseFirestore ,  marketPost , onPostAdded = {
                _isMarketPostLoading.value = false
                onPostAdded()
            } , onError = {
                _isMarketPostLoading.value = false
            })
        }
    }

    fun fetchAllMarketPosts(){
        viewModelScope.launch {
            homeScreenRepo.fetchAllMarketPosts(firebaseFirestore).catch {

            }.collect{
                _listOfMarketPosts.value = it
                Log.d("ListOfPost","Market post size is ${_listOfMarketPosts.value.size}")
            }
        }
    }

    fun addAgentPost(agentPost: AgentPost , onPostAdded: () -> Unit){
        viewModelScope.launch {
            _isUploading.value = true
            Log.d("AgentPost","Going to add agent post")
            agentPost.name = "Murli"
            homeScreenRepo.addAgentPost(firebaseFirestore , agentPost , onPosted = {
                Log.d("AgentPost","Agent Post Added successfully")
                _isUploading.value = false
                onPostAdded()
            })
        }
    }

    fun fetchAllAgentPost(){

        viewModelScope.launch {
            homeScreenRepo.fetchAllAgentPost(firebaseFirestore)
                .catch {

                }.collect{
                    Log.d("AgentPost","Agent post list size is ${_listOfAgentPost.value.size}")
                    _listOfAgentPost.value = it
                }
        }
    }

}
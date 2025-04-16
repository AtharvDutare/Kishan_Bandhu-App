package com.example.trysample.myviewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trysample.mydata.AgentPost
import com.example.trysample.myrepository.AgentDetailRepo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AgentDetailsViewModel : ViewModel() {
    private val firebaseFirestore : FirebaseFirestore  = FirebaseFirestore.getInstance()
    private val agentDetailsRepo  : AgentDetailRepo = AgentDetailRepo()

    private val _postDetails = MutableStateFlow<AgentPost>(AgentPost())
    val postDetails : StateFlow<AgentPost> get() = _postDetails.asStateFlow()


    fun fetchAgentPostDetails(agentPostId : String){
        viewModelScope.launch {
            agentDetailsRepo.fetchAgentPostDetails(firebaseFirestore , agentPostId , onPostDetailsFetched = {
                Log.d("AgentPost","Post Details fetched ${it.posId}")
                _postDetails.value = it
            })
        }
    }

}
package com.example.trysample.myrepository

import com.example.trysample.mydata.AgentPost
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AgentDetailRepo {


    suspend fun fetchAgentPostDetails(firebaseFirestore: FirebaseFirestore , agentPostId : String , onPostDetailsFetched : (AgentPost) -> Unit ){

       val postDetails =  firebaseFirestore.collection("all_agent_post").document(agentPostId).get().await()

        postDetails.toObject(AgentPost::class.java)?.let { onPostDetailsFetched(it) }

    }
}
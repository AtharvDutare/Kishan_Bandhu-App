package com.example.trysample.myrepository

import android.net.Uri
import android.util.Log
import com.example.trysample.mydata.MarketPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import androidx.core.net.toUri
import com.example.trysample.mydata.AgentPost
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class HomeScreenRepo {


    suspend fun addMarketPlacePost(
        firestore: FirebaseFirestore,
        marketPost: MarketPost, onPostAdded: () -> Unit,
        onError : () -> Unit
    ) {


        try {
            val marketPostId = firestore.collection("all_market_posts").document().id
            marketPost.postId = marketPostId


            Log.d("ImageUri","Image is ${marketPost.seedImage}")
            firestore.collection("all_market_posts").document(marketPostId).set(marketPost).await()

            onPostAdded()
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error uploading post: ${e.localizedMessage}")
            onError()
        }
    }



    fun fetchAllMarketPosts(firestore: FirebaseFirestore) : Flow<List<MarketPost>> = callbackFlow {

        val collectionReferece = firestore.collection("all_market_posts").orderBy("datePosted" , Query.Direction.DESCENDING)

        val realTimeMarketPostListener = collectionReferece.addSnapshotListener{snapshot , error->
            if(error != null){
                Log.d("MarketPost", "Error in fetching market post $error")
                close(error)
                return@addSnapshotListener
            }

            if(snapshot != null){
                Log.d("ListOfPost","snapshot is not null")
                val list = snapshot.documents.mapNotNull{
                    it.toObject(MarketPost::class.java)
                }
                trySend(list)
            }
        }

        awaitClose{realTimeMarketPostListener.remove()}
    }



    suspend fun addAgentPost(firestore: FirebaseFirestore, agentPost: AgentPost, onPosted : () -> Unit ){
        val postId = firestore.collection("all_agent_post").document().id
        agentPost.posId = postId
        firestore.collection("all_agent_post").document(postId).set(agentPost).await()

        onPosted()
    }


     fun fetchAllAgentPost(firestore: FirebaseFirestore) : Flow<List<AgentPost>> = callbackFlow{

      val collectionReference =   firestore.collection("all_agent_post")


        val realTimeListener = collectionReference.addSnapshotListener{snapshot , error->

            if(error != null){
                Log.d("AgentPost","Error in fetching agent post $error")
                close(error)
                return@addSnapshotListener
            }

            val agentPostList = snapshot?.documents?.mapNotNull { doc->
               doc.toObject(AgentPost::class.java)
            } ?: emptyList()

            trySend(agentPostList)
        }
        awaitClose{realTimeListener.remove()}

    }


}
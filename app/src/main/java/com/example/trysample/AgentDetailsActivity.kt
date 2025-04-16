package com.example.trysample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trysample.myviewmodels.AgentDetailsViewModel
import com.example.trysample.ui.screens.AgentDetailScreen

class AgentDetailsActivity : ComponentActivity() {

    private val agentDetailsViewModel : AgentDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val postId = intent.getStringExtra("post_id")
        Log.d("Post_Id","Post id is $postId")
        setContent {
            agentDetailsViewModel.fetchAgentPostDetails(postId!!)
            
            // Create a navigation controller for the activity
            val navController = rememberNavController()
            
            // Set up the navigation graph
            NavHost(
                navController = navController,
                startDestination = "agent_detail"
            ) {
                composable("agent_detail") {
                    AgentDetailScreen(
                        agentId = postId!!,
                        agentDetailsViewModel = agentDetailsViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}
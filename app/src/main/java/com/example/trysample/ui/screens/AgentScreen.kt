package com.example.trysample.ui.screens


import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
import com.example.trysample.ui.theme.HealthyGreen
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.trysample.weather.WeatherViewModel
import com.example.trysample.weatherpart.WeatherViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.trysample.AgentDetailsActivity
import com.example.trysample.R
import com.example.trysample.mydata.AgentPost
import com.example.trysample.myviewmodels.HomeScreenViewModel
import com.example.trysample.screens.AddAgentPostDialog


/**
 * AgentScreen composable that displays the AI agent interface for crop scanning and analysis.
 *
 * @param modifier Optional modifier for customizing the layout of the screen.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentScreen(
    homeScreenViewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {
    // Get the WeatherViewModel to access weather information
    val weatherViewModel: WeatherViewModel = viewModel()
    //val weatherState by weatherViewModel.weatherState.collectAsState()
    val context = LocalContext.current

    var showAgentPostDialog by remember { mutableStateOf(false) }

    val listOfAgentPost = homeScreenViewModel.listOfAgentPost.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("scan_crop_home") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Scan Plant",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Agent List",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Weather info
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }


            items(listOfAgentPost.value) { agentPost ->
                AgentPostCard(
                    agentPost = agentPost,
                    onViewDetailsClick = {
                        navController.navigate("agent_detail/${agentPost.posId}")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Scan Dialog
        if (showAgentPostDialog) {
            AddAgentPostDialog(onDismiss = {
                showAgentPostDialog = false
            }, onPostAdded = { agentPost: AgentPost ->
                showAgentPostDialog = false
                homeScreenViewModel.addAgentPost(agentPost, onPostAdded = {
                    Toast.makeText(context, "Posted Successfully", Toast.LENGTH_SHORT).show()
                })
            })
        }
    }
}

@Composable
fun AgentPostCard(
    agentPost: AgentPost,
    modifier: Modifier = Modifier,
    onViewDetailsClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Time Posted at top right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = agentPost.datePosted,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Agent Content
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Agent Photo
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                // Agent Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Agent Information",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Agent Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = agentPost.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Number of laborers
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Laborers: ${agentPost.totalLabor}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Per labor charge
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Per Labor: Rs : ${agentPost.chargePerLabor}/day",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onViewDetailsClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HealthyGreen
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Details")
                }
            }
        }
    }
}

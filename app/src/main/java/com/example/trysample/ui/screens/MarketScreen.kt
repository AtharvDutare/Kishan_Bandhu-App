package com.example.trysample.ui.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.trysample.Helper
import com.example.trysample.mydata.MarketPost
import com.example.trysample.myviewmodels.HomeScreenViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    homeScreenViewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {
    var showAddPostDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isLoading = homeScreenViewModel.isMarketPostLoading.collectAsState()


    val scope = rememberCoroutineScope()

    val listOfMarketPost = homeScreenViewModel.listOfMarketPosts.collectAsState()

    Log.d("ListOfPost","Size in composable is ${listOfMarketPost.value.size}")


    Scaffold(
        floatingActionButton = {

            if(isLoading.value){
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            }
            else{
                FloatingActionButton(
                    onClick = { showAddPostDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Post",
                        tint = Color.White
                    )
                }
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
                        text = "Crop Marketplace",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Filter chips


                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // List of Market Posts

            items(listOfMarketPost.value){post->
                SeedCard(post)
            }
        }

        // Add Post Dialog
        if (showAddPostDialog) {
            AddPostDialog(
                onDismiss = { showAddPostDialog = false },
                homeScreenViewModel,
                onPostAdded = {marketPost ->
                    showAddPostDialog = false
                    homeScreenViewModel.addMarketPlacePost(marketPost , onPostAdded = {
                        Toast.makeText(context, "Posted Successfuly", Toast.LENGTH_SHORT).show()
                    })

                }
            )
        }
    }
}

@Composable
fun SeedCard(
    post: MarketPost,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Log.d("ListOfPost","Composabe image loading is ${post.seedImage}")

            AsyncImage(model = post.seedImage, contentDescription =  null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(8.dp)
                    ))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(text = post.cropName, fontWeight = FontWeight.Bold)
                }
                Text(text = "${post.price} /kg and ${post.quantity} kg available", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = post.sellerName, color = Color.Gray, fontSize = 13.sp)

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = post.location, color = Color.Gray, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Contact seller logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Contact Seller", color = Color.White)
            }
        }
    }
}

enum class CropType {
    CORN,
    SOYBEAN,
    WHEAT
}


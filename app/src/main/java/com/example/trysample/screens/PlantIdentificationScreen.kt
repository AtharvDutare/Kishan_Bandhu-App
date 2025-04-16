package com.example.trysample.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.trysample.Helper
import com.example.trysample.R
import com.example.trysample.mydata.SimilarImageHealth
import com.example.trysample.myviewmodels.ScanCropViewModel


@Composable
fun PlantIdentificationScreen(homeScreenViewmodel: ScanCropViewModel) {

    val diseaseInformation = homeScreenViewmodel.diseaseInformation.collectAsState()

    val isAnalyzing = homeScreenViewmodel.isAnalyzing.collectAsState()

    val plantAllInformation = homeScreenViewmodel.plantHealthAllInformation.collectAsState()
    val context = LocalContext.current

    val selectedImageFromGallery = remember {
        mutableStateOf("")
    }
    val selectedImageBase64 = remember {
        mutableStateOf("")
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageFromGallery.value = uri.toString()
                selectedImageBase64.value = Helper.convertImageUriToBase64(context, uri)
            }

        })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Diagnosis Report", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Diagnosis Image Card (this is image that user uploads)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .height(180.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = if (selectedImageFromGallery.value.isNotEmpty()) selectedImageFromGallery.value else R.drawable.upload_image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                launcher.launch("image/*")
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatusChip(
                        "Plant Detected",
                        "${plantAllInformation.value?.result?.is_plant?.probability} %",
                        Color.Black
                    )
                    StatusChip(
                        "Health Alert",
                        "${plantAllInformation.value?.result?.is_healthy?.probability} %",
                        Color(0xFFFFE1C1)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            if(isAnalyzing.value){
                CircularProgressIndicator(modifier = Modifier.size(30.dp))
            }
            else{
            ActionButton(Icons.Default.Add, "Analyze", onClick = {
                homeScreenViewmodel.fetchPlantHealth(selectedImageBase64.value)
            })
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        diseaseInformation.value?.suggestions?.forEach { healthSuggestion ->
            DiseaseCard(
                title = healthSuggestion.name,
                probability = healthSuggestion.probability,
                healthSuggestion.similar_images
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // Bottom padding
    }
}

@Composable
fun StatusChip(label: String, value: String, bgColor: Color) {
    Row(
        modifier = Modifier
            .background(bgColor.copy(alpha = 0.1f), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${value.take(4)} % $label ", fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Icon(icon, contentDescription = label, tint = Color.White)
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, color = Color.White)
    }
}

@Composable
fun DiseaseCard(title: String, probability: Double, imagesInformation: List<SimilarImageHealth>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(title, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                imagesInformation.forEachIndexed {index ,  similarImageHealth ->
                    AsyncImage(
                        model = similarImageHealth.url,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    if (index != imagesInformation.lastIndex) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text("Probability: ${probability * 100} %", fontSize = 12.sp, color = Color.Gray)

        }

    }
}

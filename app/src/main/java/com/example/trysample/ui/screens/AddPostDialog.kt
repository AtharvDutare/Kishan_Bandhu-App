package com.example.trysample.ui.screens

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.trysample.Helper
import com.example.trysample.R
import com.example.trysample.mydata.MarketPost
import com.example.trysample.myviewmodels.HomeScreenViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostDialog(
    onDismiss: () -> Unit = {},
    homeScreenViewModel: HomeScreenViewModel,
    onPostAdded: (MarketPost) -> Unit = {}
) {
    val context = LocalContext.current

    var cropName by remember { mutableStateOf("") }

    var cropType by remember { mutableStateOf("") }

    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val selectedCropImage = remember {
        mutableStateOf("")
    }
    Log.d("ListOfPost","Composabe image loading is ${selectedCropImage.value}")
    val selectedImageBase64 = remember {
        mutableStateOf("")
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uri: Uri? ->
        uri?.let {
            selectedCropImage.value = uri.toString()
        }
    }


            AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add New Post",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {



                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.size(50.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }) {
                        AsyncImage(
                            model = if (selectedCropImage.value.isNotEmpty())  selectedCropImage.value else R.drawable.upload_image,
                            contentDescription = "cropimage"
                        )
                    }
                }

                // Crop Name
                OutlinedTextField(
                    value = cropType,
                    onValueChange = { cropType = it },
                    label = { Text("Crop Type") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )


                // Crop Name
                OutlinedTextField(
                    value = cropName,
                    onValueChange = { cropName = it },
                    label = { Text("Crop Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )


                // Quantity
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity (kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // Price
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price (Rs)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {


                Button(
                    onClick = {
                        val newPost = MarketPost(
                            postId = System.currentTimeMillis().toString(),
                            cropName = cropName,
                            quantity = quantity,
                            seedImage = selectedCropImage.value,
                            price = price,
                            sellerName = "Murli", // In a real app, this would come from the user profile
                            location = "Indore Rau",
                            datePosted = Helper.getCurrentDateTime()
                        )
                        onPostAdded(newPost)
                    },
                    enabled = cropName.isNotBlank() && selectedCropImage.value.isNotEmpty() &&
                            quantity.isNotBlank() && price.isNotBlank()
                ) {
                    Text("Post")
                }


        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

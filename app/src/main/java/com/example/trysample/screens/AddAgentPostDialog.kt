package com.example.trysample.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trysample.Helper
import com.example.trysample.R
import com.example.trysample.mydata.AgentPost
import com.example.trysample.navigation.BottomNavItem


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AddAgentPostDialog(
    onDismiss: () -> Unit = {},
    onPostAdded: (AgentPost) -> Unit = {}
) {
    val context = LocalContext.current

    var chargePerLabor by remember { mutableStateOf("") }

    var totalLabor by remember { mutableStateOf("") }

    var contactNumber by remember { mutableStateOf("") }
    var locationServiceOffered by remember { mutableStateOf("") }




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




                // Crop Name
                OutlinedTextField(
                    value = totalLabor,
                    onValueChange = { totalLabor = it },
                    label = { Text("Total Labor") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )


                OutlinedTextField(
                    value = chargePerLabor,
                    onValueChange = { chargePerLabor = it },
                    label = { Text(stringResource(R.string.charge_per_labor) , modifier = Modifier.fillMaxWidth()) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )


                // Quantity
                OutlinedTextField(
                    value = contactNumber,
                    onValueChange = { contactNumber = it },
                    label = { Text(stringResource(R.string.contact_details)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // Price
                OutlinedTextField(
                    value = locationServiceOffered,
                    onValueChange = { locationServiceOffered = it },
                    label = { Text(stringResource(R.string.location_service_provided)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
        },
        confirmButton = {


            Button(
                onClick = {
                    val postDetails = AgentPost("","", Helper.getCurrentDateTime(),totalLabor,chargePerLabor , locationServiceOffered )

                    onPostAdded(postDetails)
                },
                enabled = locationServiceOffered.isNotBlank() && contactNumber.isNotBlank() &&
                        totalLabor.isNotBlank() && chargePerLabor.isNotBlank()
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

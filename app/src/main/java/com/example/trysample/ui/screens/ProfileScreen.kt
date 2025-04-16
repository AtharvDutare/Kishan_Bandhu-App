package com.example.trysample.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trysample.R
import com.example.trysample.ui.theme.HealthyGreen
import com.example.trysample.ui.theme.LanguageManager
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.trysample.auth.AuthViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest

private const val TAG = "ProfileScreen"

/**
 * ProfileScreen composable that displays the user's profile information and settings.
 *
 * @param onSignOut Callback function that is invoked when the user clicks the sign out button.
 * @param modifier Optional modifier for customizing the layout of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    val languageState = LanguageManager.languageState
    val currentUser by authViewModel.currentUser.collectAsState()
    val userSelectedImageFromGallery = remember { mutableStateOf("") }
    
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        userSelectedImageFromGallery.value = uri.toString()
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(LanguageManager.getText("change_language")) },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = languageState.isEnglish,
                            onClick = { 
                                if (!languageState.isEnglish) {
                                    LanguageManager.toggleLanguage()
                                }
                                showLanguageDialog = false
                            }
                        )
                        Text(LanguageManager.getText("english"))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = !languageState.isEnglish,
                            onClick = { 
                                if (languageState.isEnglish) {
                                    LanguageManager.toggleLanguage()
                                }
                                showLanguageDialog = false
                            }
                        )
                        Text(LanguageManager.getText("hindi"))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(LanguageManager.getText("ok"))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(LanguageManager.getText("profile")) },
                actions = {
                    IconButton(onClick = { showLanguageDialog = true }) {
                        Icon(Icons.Default.Language, contentDescription = LanguageManager.getText("change_language"))
                    }
                    IconButton(onClick = onSignOut) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = LanguageManager.getText("sign_out"))
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Profile Header
            item {
                ProfileHeader(currentUser, launcher, userSelectedImageFromGallery)
            }

            // Statistics
            item {
                StatisticsCards()
            }

            // Recent Disease Reports
            item {
                Text(
                    text = LanguageManager.getText("recent_disease_reports"),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Disease Report Items
            items(sampleDiseaseReports) { report ->
                DiseaseReportCard(report)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Update Profile Button
            item {
                OutlinedButton(
                    onClick = {
                        if(userSelectedImageFromGallery.value.isNotEmpty()){
                            Log.d("UserImage","UserImage is ${userSelectedImageFromGallery.value}")
                            authViewModel.updateImage(userSelectedImageFromGallery.value)
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(text = LanguageManager.getText("update_profile"))
                }
            }

            // Sign Out Button
            item {
                Button(
                    onClick = onSignOut,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(LanguageManager.getText("sign_out"))
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    currentUser: FirebaseUser?,
    launcher: androidx.activity.result.ActivityResultLauncher<String>,
    userSelectedImageFromGallery: androidx.compose.runtime.MutableState<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .clickable {
                    launcher.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
            if (userSelectedImageFromGallery.value.isNotEmpty()) {
                AsyncImage(
                    model = userSelectedImageFromGallery.value,
                    contentDescription = "",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(8.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = when {
                !currentUser?.displayName.isNullOrEmpty() -> currentUser.displayName!!
                !currentUser?.email.isNullOrEmpty() -> currentUser.email!!.substringBefore('@')
                else -> LanguageManager.getText("user")
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = LanguageManager.getText("farm_owner"),
            style = MaterialTheme.typography.titleMedium
        )
        
        Text(
            text = LanguageManager.getText("user"),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun StatisticsCards() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatCard(
            icon = Icons.Default.Agriculture,
            value = "5",
            label = LanguageManager.getText("acres")
        )
        
        StatCard(
            icon = Icons.Default.CameraAlt,
            value = "12",
            label = LanguageManager.getText("scans")
        )
        
        StatCard(
            icon = Icons.Default.CheckCircle,
            value = "8",
            label = LanguageManager.getText("success")
        )
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun DiseaseReportCard(report: DiseaseReport) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = report.disease,
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = "${LanguageManager.getText("detected_on")}: ${report.date}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = report.status,
                style = MaterialTheme.typography.bodyMedium,
                color = if (report.status == LanguageManager.getText("treated")) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.error
            )
        }
    }
}

data class DiseaseReport(
    val disease: String,
    val date: String,
    val status: String
)

private val sampleDiseaseReports = listOf(
    DiseaseReport(
        disease = "Leaf Blight",
        date = "2024-03-15",
        status = LanguageManager.getText("treated")
    ),
    DiseaseReport(
        disease = "Root Rot",
        date = "2024-03-10",
        status = LanguageManager.getText("active")
    )
) 
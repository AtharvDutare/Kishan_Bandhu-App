package com.example.trysample.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trysample.R
import com.example.trysample.ScanCrop
import com.example.trysample.ui.theme.HealthyGreen
import com.example.trysample.ui.theme.LanguageManager
import com.example.trysample.navigation.BottomNavItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Scan(
    val id: String,
    val cropName: String,
    val date: String,
    val status: String
)

data class Alert(
    val icon: ImageVector,
    val titleKey: String,
    val descriptionKey: String,
    val timestamp: LocalDateTime
) {
    fun getFormattedTime(): String {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
        return timestamp.format(formatter)
    }
}

// Sample alerts
val homeAlerts = listOf(
    Alert(
        icon = Icons.Default.Warning,
        titleKey = "disease_detected",
        descriptionKey = "leaf_blight_detected",
        timestamp = LocalDateTime.now().minusDays(1)
    ),
    Alert(
        icon = Icons.Default.Info,
        titleKey = "weather_alert",
        descriptionKey = "heavy_rain_expected",
        timestamp = LocalDateTime.now().minusHours(6)
    ),
    Alert(
        icon = Icons.Default.CheckCircle,
        titleKey = "treatment_successful",
        descriptionKey = "fungicide_completed",
        timestamp = LocalDateTime.now().minusDays(2)
    )
)

// Sample scans
val recentScans = listOf(
    Scan(
        id = "1",
        cropName = "Corn",
        date = "May 15, 2024",
        status = "Healthy"
    ),
    Scan(
        id = "2",
        cropName = "Soybeans",
        date = "May 12, 2024",
        status = "Diseased"
    ),
    Scan(
        id = "3",
        cropName = "Wheat",
        date = "May 10, 2024",
        status = "Healthy"
    )
)

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onScanClick: () -> Unit = {},
    onViewAllScansClick: () -> Unit = {}
) {
    val context = LocalContext.current
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Welcome Section
        Text(
            text = LanguageManager.getText("smart_farming"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = LanguageManager.getText("welcome_dashboard"),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Scan Button
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = LanguageManager.getText("scan_crops"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { 
                        navController.navigate(BottomNavItem.Agent.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(LanguageManager.getText("scan_now"))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Quick Actions Section
        Text(
            text = LanguageManager.getText("quick_actions"),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickActionButton(
                icon = Icons.Default.WbCloudy,
                label = LanguageManager.getText("weather"),
                onClick = { 
                    navController.navigate(BottomNavItem.Weather.route)
                },
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                icon = Icons.Default.Grass,
                label = LanguageManager.getText("seeds"),
                onClick = {
                    navController.navigate(BottomNavItem.Stats.route)
                },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Recent Alerts Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LanguageManager.getText("recent_alerts"),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = {
                Toast.makeText(context, LanguageManager.getText("no_more_alerts"), Toast.LENGTH_SHORT).show()
            }) {
                Text(LanguageManager.getText("view_all"))
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Show only the first 3 alerts in the HomeScreen
        homeAlerts.forEach { alert ->
            HomeAlertItem(
                icon = alert.icon,
                title = LanguageManager.getText(alert.titleKey),
                description = LanguageManager.getText(alert.descriptionKey),
                timestamp = alert.getFormattedTime()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Recent Scans Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LanguageManager.getText("recent_scans"),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onViewAllScansClick) {
                Text(LanguageManager.getText("view_all"))
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Recent Scans
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recentScans) { scan ->
                ScanCard(scan = scan)
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ScanCard(scan: Scan) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = scan.cropName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = scan.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            when (scan.status) {
                                "Healthy" -> HealthyGreen
                                "Diseased" -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = LanguageManager.getText(scan.status.lowercase()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun HomeAlertItem(
    icon: ImageVector,
    title: String,
    description: String,
    timestamp: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 
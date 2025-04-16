package com.example.trysample.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Alerts") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(allAlerts) { alert ->
                AlertItem(
                    icon = alert.icon,
                    title = alert.title,
                    description = alert.description,
                    timestamp = alert.getFormattedTime()
                )
            }
        }
    }
}

@Composable
fun AlertItem(
    icon: ImageVector,
    title: String,
    description: String,
    timestamp: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Alert Icon
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Alert Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class GeneralAlert(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    fun getFormattedTime(): String {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
        return timestamp.format(formatter)
    }
}

val allAlerts = listOf(
    GeneralAlert(
        icon = Icons.Default.WbCloudy,
        title = "Weather Warning",
        description = "Heavy Rain Expected Tonight",
        timestamp = LocalDateTime.now()
    ),
    GeneralAlert(
        icon = Icons.Default.Cloud,
        title = "Cloudy Weather",
        description = "Clouds after 2 hours",
        timestamp = LocalDateTime.now().plusHours(2)
    ),
    GeneralAlert(
        icon = Icons.Default.WaterDrop,
        title = "Irrigation Alert",
        description = "Field 2 needs watering",
        timestamp = LocalDateTime.now().minusHours(1)
    ),
    GeneralAlert(
        icon = Icons.Default.BugReport,
        title = "Pest Detection",
        description = "Possible pest infestation in Corn Field",
        timestamp = LocalDateTime.now().minusHours(3)
    ),
    GeneralAlert(
        icon = Icons.Default.WbSunny,
        title = "High Temperature",
        description = "Temperature expected to reach 35°C",
        timestamp = LocalDateTime.now().plusHours(4)
    ),
    GeneralAlert(
        icon = Icons.Default.Landscape,
        title = "Soil Moisture Low",
        description = "Soybean field needs attention",
        timestamp = LocalDateTime.now().minusHours(2)
    )
) 
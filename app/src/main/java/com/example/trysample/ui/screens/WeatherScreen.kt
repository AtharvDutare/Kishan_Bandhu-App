package com.example.trysample.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trysample.networkresponse.NetworkResponse
import com.example.trysample.weatherpart.WeatherViewModel
import com.example.trysample.weatherpart.datamodel.WeatherModel


@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier

) {
    var searchQuery by remember { mutableStateOf("delhi") }

    val weatherResult=viewModel.weatherResults.observeAsState()
    //viewModel.getLocation(searchQuery)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly

        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    //Log.i("WeatherScreenCompose",it)
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text(text = "Search for any location")
                }
                ,
//                placeholder = { Text("Search weather") },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = "Search"
//                    )
//                },
                singleLine = true
            )
            IconButton(onClick = { viewModel.getLocation(searchQuery) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for any Location"
                )
            }
        }

        when (val result = weatherResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                expi1(result.data)
            }

            null -> {}
        }
    }
}
@Composable
fun expi1(data:WeatherModel) {
    Spacer(modifier = Modifier.height(24.dp))

    // Weather Conditions
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Weather Conditions",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = data.location.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${data.current.temp_f}°F",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = data.current.condition.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetail(
                    icon = Icons.Default.WaterDrop,
                    label = "Humidity",
                    value = data.current.humidity
                )
                WeatherDetail(
                    icon = Icons.Default.Air,
                    label = "Wind",
                    value = data.current.wind_mph +" mph"
                )
                WeatherDetail(
                    icon = Icons.Default.Umbrella,
                    label = "Cloud",
                    value = data.current.cloud
                )
            }
        }
    }

    // Recent Alerts
    Text(
        text = "Recent Alerts",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 16.dp)
    )

    LazyColumn {
        items(sampleAlerts) { alert ->
            AlertItem(alert = alert)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun WeatherDetail(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AlertItem(
    alert: WeatherAlert,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (alert.type) {
                    AlertType.HIGH_RISK -> Icons.Default.Warning
                    AlertType.MODERATE -> Icons.Default.Info
                },
                contentDescription = alert.title,
                tint = when (alert.type) {
                    AlertType.HIGH_RISK -> Color.Red
                    AlertType.MODERATE -> Color.Yellow
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = alert.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

enum class AlertType {
    HIGH_RISK,
    MODERATE
}

data class WeatherAlert(
    val title: String,
    val location: String,
    val type: AlertType
)

private val sampleAlerts = listOf(
    WeatherAlert(
        "High risk of Powdery Mildew detected",
        "Field B3 - North Section",
        AlertType.HIGH_RISK
    ),
    WeatherAlert(
        "Moderate risk of Leaf Rust development",
        "Field A1 - West Section",
        AlertType.MODERATE
    )
) 
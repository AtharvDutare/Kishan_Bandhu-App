package com.example.trysample.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trysample.myviewmodels.ScanCropViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanCropHomeScreen(
    scanCropViewmodel: ScanCropViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Krishi Bandhu")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        navController.navigate("Scan")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Scan"
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate("Identification")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Identification")
                    }
                }
            }
        }
    ) {paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)){

            NavHost(navController = navController, startDestination = "Scan"){
                composable("Scan"){
                    ChatBot(scanCropViewModel = scanCropViewmodel)
                }
                composable("Identification"){
                    PlantIdentificationScreen(homeScreenViewmodel = scanCropViewmodel)
                }
            }
        }
    }
}
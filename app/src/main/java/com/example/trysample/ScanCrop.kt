package com.example.trysample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trysample.myviewmodels.ScanCropViewModel
import com.example.trysample.screens.ScanCropHomeScreen

class ScanCrop : ComponentActivity() {

    val scanCropViewModel : ScanCropViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScanCropHomeScreen(scanCropViewModel)
        }
    }
}
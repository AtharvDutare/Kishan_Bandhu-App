package com.example.trysample.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trysample.R
import com.example.trysample.ui.theme.LanguageManager
import kotlinx.coroutines.delay


@Preview
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit = {}
) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        startAnimation = true
        delay(2000)
        onSplashComplete()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo_600x600_circular),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .scale(if (startAnimation) 1f else 0.5f)
                    .alpha(if (startAnimation) 1f else 0f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = LanguageManager.getText("smart_farming"),
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(if (startAnimation) 1f else 0f)
                    .scale(if (startAnimation) 1f else 0.8f)
            )
        }
    }
}

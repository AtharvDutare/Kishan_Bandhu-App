package com.example.trysample.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Green80,
    onPrimary = Color.White,
    primaryContainer = GreenGrey80,
    onPrimaryContainer = Color.White,
    secondary = LightGreen80,
    onSecondary = Color.White,
    background = BackgroundGray,
    surface = Color.White,
    onSurface = TextBlack,
    surfaceVariant = Color.White.copy(alpha = 0.95f),
    onSurfaceVariant = TextBlack.copy(alpha = 0.7f)
)

private val DarkColorScheme = darkColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = GreenGrey40,
    onPrimaryContainer = Color.White,
    secondary = LightGreen40,
    onSecondary = Color.White,
    background = Color.Black,
    surface = Color(0xFF1C1B1F),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1C1B1F).copy(alpha = 0.95f),
    onSurfaceVariant = Color.White.copy(alpha = 0.7f)
)

@Composable
fun TrySampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
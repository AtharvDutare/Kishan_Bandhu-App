package com.example.trysample.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.trysample.ui.theme.LanguageManager

sealed class BottomNavItem(
    val route: String,
    val titleKey: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        titleKey = "home",
        icon = Icons.Default.Home
    )
    object Weather : BottomNavItem(
        route = "weather",
        titleKey = "weather",
        icon = Icons.Default.WbSunny
    )
    object Agent : BottomNavItem(
        route = "agent",
        titleKey = "agent",
        icon = Icons.Default.SmartToy
    )
    object Stats : BottomNavItem(
        route = "stats",
        titleKey = "market",
        icon = Icons.Default.BarChart
    )
    object Profile : BottomNavItem(
        route = "profile",
        titleKey = "profile",
        icon = Icons.Default.Person
    )
    
    val title: String
        get() = LanguageManager.getText(titleKey)
} 
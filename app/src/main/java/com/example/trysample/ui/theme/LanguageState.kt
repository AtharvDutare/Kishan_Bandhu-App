package com.example.trysample.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class LanguageState(
    val isEnglish: Boolean = true
)

val LocalLanguageState = compositionLocalOf { LanguageState() }

object LanguageManager {
    var languageState by mutableStateOf(LanguageState())
        private set

    fun toggleLanguage() {
        languageState = languageState.copy(isEnglish = !languageState.isEnglish)
    }

    fun getText(key: String): String {
        return if (languageState.isEnglish) {
            englishText[key] ?: key
        } else {
            hindiText[key] ?: key
        }
    }

    private val englishText = mapOf(
        // Navigation
        "home" to "Home",
        "weather" to "Weather",
        "agent" to "Agent",
        "stats" to "Stats",
        "profile" to "Profile",
        "market" to "Market",
        
        // Profile Screen
        "smart_farming" to "Smart Farming Solutions",
        "farm_owner" to "Farm Owner",
        "user" to "User",
        "active" to "Active",
        "acres" to "Acres",
        "scans" to "Scans",
        "success" to "Success",
        "recent_disease_reports" to "Recent Disease Reports",
        "update_profile" to "Update Profile",
        "treated" to "Treated",
        "detected_on" to "Detected On",
        "ok" to "OK",
        "sign_out" to "Sign Out",
        "change_language" to "Change Language",
        "english" to "English",
        "hindi" to "Hindi",
        
        // Home Screen
        "quick_actions" to "Quick Actions",
        "seeds" to "Seeds",
        "recent_alerts" to "Recent Alerts",
        "view_all" to "View All",
        "scan_now" to "Scan Now",
        "disease_detected" to "Disease Detected",
        "weather_alert" to "Weather Alert",
        "treatment_successful" to "Treatment Successful",
        "leaf_blight_detected" to "Leaf blight detected in your corn field",
        "heavy_rain_expected" to "Heavy rain expected in the next 24 hours",
        "fungicide_completed" to "Fungicide application completed successfully",
        "welcome_dashboard" to "Welcome to your farming dashboard",
        "scan_crops" to "Scan your crops for disease detection",
        "recent_scans" to "Recent Scans",
        "no_more_alerts" to "No more alerts",
        "healthy" to "Healthy",
        "diseased" to "Diseased",
        
        // Disease Names
        "leaf_blight" to "Leaf Blight",
        "root_rot" to "Root Rot"
    )

    private val hindiText = mapOf(
        // Navigation
        "home" to "होम",
        "weather" to "मौसम",
        "agent" to "एजेंट",
        "stats" to "आँकड़े",
        "profile" to "प्रोफ़ाइल",
        "market" to "बाजार",
        
        // Profile Screen
        "smart_farming" to "स्मार्ट फार्मिंग समाधान",
        "farm_owner" to "फार्म मालिक",
        "user" to "उपयोगकर्ता",
        "active" to "सक्रिय",
        "acres" to "एकड़",
        "scans" to "स्कैन",
        "success" to "सफल",
        "recent_disease_reports" to "हाल के रोग रिपोर्ट",
        "update_profile" to "प्रोफ़ाइल अपडेट करें",
        "treated" to "उपचारित",
        "detected_on" to "पता लगाया गया",
        "ok" to "ठीक है",
        "sign_out" to "साइन आउट",
        "change_language" to "भाषा बदलें",
        "english" to "अंग्रेजी",
        "hindi" to "हिंदी",
        
        // Home Screen
        "quick_actions" to "त्वरित कार्य",
        "seeds" to "बीज",
        "recent_alerts" to "हाल के अलर्ट",
        "view_all" to "सभी देखें",
        "scan_now" to "अभी स्कैन करें",
        "disease_detected" to "रोग का पता चला",
        "weather_alert" to "मौसम अलर्ट",
        "treatment_successful" to "उपचार सफल",
        "leaf_blight_detected" to "आपके मकई के खेत में पत्ती का ब्लाइट पाया गया",
        "heavy_rain_expected" to "अगले 24 घंटों में भारी बारिश की संभावना",
        "fungicide_completed" to "फंगीसाइड का उपयोग सफलतापूर्वक पूरा हुआ",
        "welcome_dashboard" to "अपने फार्मिंग डैशबोर्ड में आपका स्वागत है",
        "scan_crops" to "रोग पहचान के लिए अपनी फसलों को स्कैन करें",
        "recent_scans" to "हाल के स्कैन",
        "no_more_alerts" to "कोई और अलर्ट नहीं",
        "healthy" to "स्वस्थ",
        "diseased" to "रोगग्रस्त",
        
        // Disease Names
        "leaf_blight" to "पत्ती का ब्लाइट",
        "root_rot" to "जड़ सड़न"
    )
} 
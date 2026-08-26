package com.example.gymtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.gymtrack.ui.navigation.GymTrackNavGraph
import com.example.gymtrack.ui.theme.GymTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // --- ELEVACIÓN DE ESTADO GLOBAL ---
            var isDarkMode by remember { mutableStateOf(true) }
            var currentLanguage by remember { mutableStateOf("Español") }
            var isNotificationsEnabled by remember { mutableStateOf(true) }
            var weightUnit by remember { mutableStateOf("Lb") } // Estado compartido de peso
            val favoriteIds = remember { mutableStateListOf<String>() }

            GymTrackTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    GymTrackNavGraph(
                        navController = navController,
                        isDarkMode = isDarkMode,
                        onDarkModeChange = { isDarkMode = it },
                        currentLanguage = currentLanguage,
                        onLanguageChange = { currentLanguage = it },
                        isNotificationsEnabled = isNotificationsEnabled,
                        onNotificationsChange = { isNotificationsEnabled = it },
                        weightUnit = weightUnit,
                        onWeightUnitChange = { weightUnit = it },
                        favoriteIds = favoriteIds
                    )
                }
            }
        }
    }
}

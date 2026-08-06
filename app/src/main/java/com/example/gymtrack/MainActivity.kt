package com.example.gymtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.gymtrack.ui.screens.HomeScreen
import com.example.gymtrack.ui.theme.GymTrackTheme
import androidx.compose.foundation.layout.padding

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            GymTrackTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->

                    HomeScreen(
                        modifier = Modifier.padding(paddingValues)
                    )

                }

            }
        }
    }
}
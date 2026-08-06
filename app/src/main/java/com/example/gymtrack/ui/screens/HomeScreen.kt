package com.example.gymtrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "👋 Bienvenido",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "GymTrack",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Tu entrenador personal",
            style = MaterialTheme.typography.bodyLarge
        )

    }
}
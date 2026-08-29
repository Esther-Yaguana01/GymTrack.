package com.example.gymtrack.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import android.net.Uri
import com.example.gymtrack.R
import com.example.gymtrack.ui.screens.ExerciseDetailScreen
import com.example.gymtrack.ui.screens.FavoritesScreen
import com.example.gymtrack.ui.screens.HomeScreen
import com.example.gymtrack.ui.screens.ProfileScreen
import com.example.gymtrack.ui.screens.SettingScreen
import com.example.gymtrack.ui.viewmodels.ExerciseUiState
import com.example.gymtrack.ui.viewmodels.ExerciseViewModel
import com.example.gymtrack.ui.viewmodels.SettingsViewModel

@Composable
fun GymTrackNavGraph(
    navController: NavHostController,
    exerciseViewModel: ExerciseViewModel,
    settingsViewModel: SettingsViewModel
) {
    val settings by settingsViewModel.settings.collectAsState()
    val exerciseUiState by exerciseViewModel.uiState.collectAsState()
    val favorites by exerciseViewModel.favorites.collectAsState()
    val searchQuery by exerciseViewModel.searchQuery.collectAsState()
    val selectedCategory by exerciseViewModel.selectedCategory.collectAsState()

    // Sincronizar el idioma con el ExerciseViewModel
    androidx.compose.runtime.LaunchedEffect(settings.language) {
        exerciseViewModel.setLanguage(settings.language)
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(route = "home") {
            when (val state = exerciseUiState) {
                is ExerciseUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is ExerciseUiState.Success -> {
                    HomeScreen(
                        exercises = state.exercises,
                        favoriteIds = favorites.map { it.id },
                        onToggleFavorite = { exercise -> exerciseViewModel.toggleFavorite(exercise) },
                        currentLanguage = settings.language,
                        weightUnit = settings.weightUnit,
                        onExerciseClick = { exerciseId ->
                                                    try {
                                                        navController.navigate("details/${Uri.encode(exerciseId)}")
                                                    } catch (e: Exception) {
                                                        android.util.Log.e("NavGraph", "navigate failed for id=$exerciseId", e)
                                                    }
                                                },
                        navController = navController,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { exerciseViewModel.onSearchQueryChange(it) },
                        selectedCategory = selectedCategory,
                        onCategoryChange = { exerciseViewModel.onCategoryChange(it) }
                    )
                }
                is ExerciseUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), 
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = state.message, 
                                color = MaterialTheme.colorScheme.error, 
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { exerciseViewModel.loadExercises() }) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                }
            }
        }

        composable(route = "favorites") {
            FavoritesScreen(
                favoriteExercises = favorites,
                currentLanguage = settings.language,
                weightUnit = settings.weightUnit,
                onExerciseClick = { exerciseId ->
                                    try {
                                        navController.navigate("details/${Uri.encode(exerciseId)}")
                                    } catch (e: Exception) {
                                        android.util.Log.e("NavGraph", "navigate failed for id=$exerciseId", e)
                                    }
                                },
                navController = navController
            )
        }

        composable(route = "settings") {
            SettingScreen(
                navController = navController,
                isDarkMode = settings.isDarkMode,
                onDarkModeChange = { settingsViewModel.toggleDarkMode(it) },
                currentLanguage = settings.language,
                onLanguageChange = { settingsViewModel.updateLanguage(it) },
                isNotificationsEnabled = settings.isNotificationsEnabled,
                onNotificationsChange = { settingsViewModel.toggleNotifications(it) },
                weightUnit = settings.weightUnit,
                onWeightUnitChange = { settingsViewModel.updateWeightUnit(it) }
            )
        }

        composable(route = "profile") {
            ProfileScreen(
                navController = navController,
                settingsViewModel = settingsViewModel
            )
        }

        composable(route = "details/{exerciseId}") { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId")
            val decodedId = exerciseId?.let { Uri.decode(it) }
            val exerciseState = exerciseUiState
            if (exerciseState is ExerciseUiState.Success) {
                val selectedExercise = exerciseState.exercises.find { it.id == decodedId }
                if (selectedExercise != null) {
                    ExerciseDetailScreen(
                        exercise = selectedExercise,
                        isFavorite = favorites.any { it.id == selectedExercise.id },
                        weightUnit = settings.weightUnit,
                        onToggleFavorite = { exerciseViewModel.toggleFavorite(selectedExercise) },
                        onBackClick = { navController.popBackStack() }
                    )
                } else {
                    // Ejercicio no encontrado — mostramos mensaje en lugar de dejar la composable vacía (evita estados inválidos)
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.exercise_not_found), color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            } else {
                // Si no hay datos todavía mostramos un indicador mínimo
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

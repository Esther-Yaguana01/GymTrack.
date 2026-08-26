package com.example.gymtrack.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.gymtrack.ui.screens.HomeScreen
import com.example.gymtrack.ui.screens.FavoritesScreen
import com.example.gymtrack.model.Exercise
import com.example.gymtrack.ui.screens.SettingScreen
import com.example.gymtrack.ui.screens.ExerciseDetailScreen

val sampleExercises = listOf(
    Exercise(
        id = "1",
        name = "Press de Banca",
        targetMuscle = "Pecho",
        imageUrl = "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?q=80&w=2070",
        equipment = "Barra y banco plano",
        difficulty = "Intermedio",
        sets = "4",
        reps = "10-12",
        rest = "90s",
        calories = "120 kcal",
        description = "1. Acuéstate en el banco con los pies firmes en el suelo. 2. Baja la barra al pecho de forma controlada. 3. Empuja hacia arriba explosivamente extendiendo los brazos."
    ),
    Exercise(
        id = "2",
        name = "Sentadillas",
        targetMuscle = "Piernas",
        imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=2070",
        equipment = "Barra o Mancuernas",
        difficulty = "Intermedio",
        sets = "4",
        reps = "12",
        rest = "90s",
        calories = "150 kcal",
        description = "1. Coloca la barra sobre tus trapecios. 2. Baja la cadera rompiendo el paralelo de las rodillas. 3. Sube manteniendo el core firme y la espalda recta."
    ),
    Exercise(
        id = "3",
        name = "Dominadas",
        targetMuscle = "Espalda",
        imageUrl = "https://images.unsplash.com/photo-1526506118085-60ce8714f8c5?q=80&w=2000",
        equipment = "Barra fija",
        difficulty = "Avanzado",
        sets = "4",
        reps = "8-10",
        rest = "120s",
        calories = "100 kcal",
        description = "1. Sujétate de la barra con agarre prono. 2. Tira de tu cuerpo hacia arriba hasta que la barbilla pase la barra. 3. Baja lentamente controlando el descenso."
    ),
    Exercise(
        id = "4",
        name = "Press Militar",
        targetMuscle = "Hombros",
        imageUrl = "https://images.unsplash.com/photo-1541534741688-6078c6bc35e5?q=80&w=2070",
        equipment = "Barra o Mancuernas",
        difficulty = "Intermedio",
        sets = "4",
        reps = "10",
        rest = "90s",
        calories = "110 kcal",
        description = "1. Sujeta la barra a la altura de los hombros. 2. Empuja el peso verticalmente sobre tu cabeza. 3. Bloquea los codos arriba y baja con control al punto inicial."
    ),
    Exercise(
        id = "5",
        name = "Peso Muerto",
        targetMuscle = "Espalda/Piernas",
        imageUrl = "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?q=80&w=2070",
        equipment = "Barra olímpica",
        difficulty = "Avanzado",
        sets = "3",
        reps = "8",
        rest = "120s",
        calories = "160 kcal",
        description = "1. Colócate con los pies a la anchura de los hombros. 2. Agarra la barra y mantén la espalda recta. 3. Levántate extendiendo caderas y rodillas simultáneamente."
    ),
    Exercise(
        id = "6",
        name = "Curl de Bíceps",
        targetMuscle = "Brazos",
        imageUrl = "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=2070",
        equipment = "Mancuernas",
        difficulty = "Principiante",
        sets = "3",
        reps = "12",
        rest = "60s",
        calories = "80 kcal",
        description = "1. Sujeta las mancuernas con las palmas hacia adelante. 2. Flexiona los codos llevando el peso hacia los hombros. 3. Baja lentamente a la posición inicial."
    ),
    Exercise(
        id = "7",
        name = "Fondos de Tríceps",
        targetMuscle = "Brazos/Pecho",
        imageUrl = "https://images.unsplash.com/photo-1532384748853-8f54a8f476e2?q=80&w=2070",
        equipment = "Barras paralelas",
        difficulty = "Intermedio",
        sets = "3",
        reps = "10",
        rest = "60s",
        calories = "85 kcal",
        description = "1. Sujétate de las barras paralelas con los brazos extendidos. 2. Baja el cuerpo flexionando los codos hasta los 90 grados. 3. Empuja hacia arriba volviendo a la posición inicial."
    ),
    Exercise(
        id = "8",
        name = "Zancadas / Lunges",
        targetMuscle = "Piernas",
        imageUrl = "https://images.unsplash.com/photo-1434608519344-49d77a699e1d?q=80&w=2074",
        equipment = "Mancuernas",
        difficulty = "Principiante",
        sets = "4",
        reps = "12 por pierna",
        rest = "90s",
        calories = "130 kcal",
        description = "1. Da un paso largo hacia adelante con una pierna. 2. Baja la cadera hasta que ambas rodillas formen un ángulo de 90 grados. 3. Empuja con el pie delantero para volver al inicio."
    )
)

@Composable
fun GymTrackNavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    isNotificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    weightUnit: String,
    onWeightUnitChange: (String) -> Unit,
    favoriteIds: SnapshotStateList<String>
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(route = "home") {
            HomeScreen(
                exercises = sampleExercises,
                favoriteIds = favoriteIds,
                onToggleFavorite = { id ->
                    if (favoriteIds.contains(id)) favoriteIds.remove(id) else favoriteIds.add(id)
                },
                currentLanguage = currentLanguage,
                weightUnit = weightUnit,
                onExerciseClick = { exerciseId ->
                    navController.navigate("details/$exerciseId")
                },
                navController = navController
            )
        }

        composable(route = "favorites") {
            val favoriteList = sampleExercises.filter { favoriteIds.contains(it.id) }
            FavoritesScreen(
                favoriteExercises = favoriteList,
                currentLanguage = currentLanguage,
                onExerciseClick = { exerciseId ->
                    navController.navigate("details/$exerciseId")
                },
                navController = navController
            )
        }

        composable(route = "settings") {
            SettingScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                onDarkModeChange = onDarkModeChange,
                currentLanguage = currentLanguage,
                onLanguageChange = onLanguageChange,
                isNotificationsEnabled = isNotificationsEnabled,
                onNotificationsChange = onNotificationsChange,
                weightUnit = weightUnit,
                onWeightUnitChange = onWeightUnitChange
            )
        }

        composable(route = "details/{exerciseId}") { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId")
            val selectedExercise = sampleExercises.find { it.id == exerciseId }

            if (selectedExercise != null) {
                ExerciseDetailScreen(
                    exercise = selectedExercise,
                    isFavorite = favoriteIds.contains(selectedExercise.id),
                    onToggleFavorite = {
                        if (favoriteIds.contains(selectedExercise.id)) {
                            favoriteIds.remove(selectedExercise.id)
                        } else {
                            favoriteIds.add(selectedExercise.id)
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

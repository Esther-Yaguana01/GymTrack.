package com.example.gymtrack.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.gymtrack.model.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    exercises: List<Exercise>,
    favoriteIds: List<String>,
    onToggleFavorite: (String) -> Unit,
    currentLanguage: String,
    onExerciseClick: (String) -> Unit,
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }
    val categories = remember(currentLanguage) {
        if (currentLanguage == "Español") {
            listOf("Todos", "Pecho", "Espalda", "Piernas", "Hombros")
        } else {
            listOf("All", "Chest", "Back", "Legs", "Shoulders")
        }
    }
    var selectedCategory by remember(currentLanguage) { mutableStateOf(categories[0]) }

    val filteredExercises = exercises.filter { exercise ->
        val matchesSearch = exercise.name.contains(searchQuery, ignoreCase = true) ||
                exercise.targetMuscle.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == categories[0] || 
                exercise.targetMuscle.equals(selectedCategory, ignoreCase = true)
        matchesSearch && matchesCategory
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavigationBar(navController, currentLanguage)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "GymTrack",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (currentLanguage == "Español") "Bienvenido 👋" else "Welcome 👋",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(if (currentLanguage == "Español") "Buscar ejercicios..." else "Search exercises...", color = Color.Gray) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFBB86FC),
                            selectedLabelColor = Color.Black
                        ),
                        border = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredExercises) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        isFavorite = favoriteIds.contains(exercise.id),
                        onToggleFavorite = { onToggleFavorite(exercise.id) },
                        onClick = { onExerciseClick(exercise.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    val heartColor by animateColorAsState(
        targetValue = if (isFavorite) Color.Red else Color.White,
        animationSpec = spring(stiffness = 500f)
    )
    val heartScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 400f)
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = exercise.imageUrl,
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.95f)),
                            startY = 300f
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .clickable { onToggleFavorite() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = heartColor,
                    modifier = Modifier.size(24.dp).scale(heartScale)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = exercise.targetMuscle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFBB86FC),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentLanguage: String) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val labels = if (currentLanguage == "Español") {
        listOf("Inicio", "Favoritos", "Ajustes")
    } else {
        listOf("Home", "Favorites", "Settings")
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(labels[0]) },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                }
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFBB86FC))
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            label = { Text(labels[1]) },
            selected = currentRoute == "favorites",
            onClick = {
                if (currentRoute != "favorites") navController.navigate("favorites")
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFBB86FC))
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text(labels[2]) },
            selected = currentRoute == "settings",
            onClick = {
                if (currentRoute != "settings") navController.navigate("settings")
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFBB86FC))
        )
    }
}

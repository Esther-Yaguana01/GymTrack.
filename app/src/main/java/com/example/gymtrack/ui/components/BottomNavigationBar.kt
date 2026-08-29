package com.example.gymtrack.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gymtrack.R

@Composable
fun BottomNavigationBar(navController: NavController, currentLanguage: String) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_home)) },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                }
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.primary)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_favorites)) },
            selected = currentRoute == "favorites",
            onClick = {
                if (currentRoute != "favorites") navController.navigate("favorites")
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.primary)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_settings)) },
            selected = currentRoute == "settings",
            onClick = {
                if (currentRoute != "settings") navController.navigate("settings")
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.primary)
        )
    }
}

package com.example.gymtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.gymtrack.data.local.AppDatabase
import com.example.gymtrack.data.local.SettingsManager
import com.example.gymtrack.data.remote.RetrofitInstance
import com.example.gymtrack.data.repository.ExerciseRepository
import com.example.gymtrack.data.repository.UserRepository
import com.example.gymtrack.ui.navigation.GymTrackNavGraph
import com.example.gymtrack.ui.theme.GymTrackTheme
import com.example.gymtrack.ui.viewmodels.ExerciseViewModel
import com.example.gymtrack.ui.viewmodels.SettingsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "gymtrack-db"
        )
        .fallbackToDestructiveMigration()
        .build()
        
        val exerciseRepository = ExerciseRepository(
            api = RetrofitInstance.api,
            dao = database.exerciseDao()
        )

        val userRepository = UserRepository()
        val settingsManager = SettingsManager(applicationContext)

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return SettingsViewModel(settingsManager, userRepository) as T
                    }
                }
            )
            
            val exerciseViewModel: ExerciseViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return ExerciseViewModel(exerciseRepository) as T
                    }
                }
            )

            val settingsState = settingsViewModel.settings.collectAsState()
            val currentLanguage = settingsState.value.language

            // Sincronización robusta del Locale
            val locale = if (currentLanguage.lowercase().contains("eng")) Locale("en") else Locale("es")
            Locale.setDefault(locale)

            // Aplicar al configuration de resources para no reemplazar LocalContext (evita romper ActivityResult)
            val resConfig = resources.configuration
            resConfig.setLocale(locale)
            resources.updateConfiguration(resConfig, resources.displayMetrics)

            GymTrackTheme(darkTheme = settingsState.value.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    GymTrackNavGraph(
                        navController = navController,
                        exerciseViewModel = exerciseViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}

package com.example.gymtrack.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtrack.data.repository.ExerciseRepository
import com.example.gymtrack.model.Exercise
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ExerciseUiState {
    object Loading : ExerciseUiState()
    data class Success(val exercises: List<Exercise>) : ExerciseUiState()
    data class Error(val message: String) : ExerciseUiState()
}

class ExerciseViewModel(private val repository: ExerciseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ExerciseUiState>(ExerciseUiState.Loading)
    val uiState: StateFlow<ExerciseUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _favorites = MutableStateFlow<List<Exercise>>(emptyList())
    val favorites: StateFlow<List<Exercise>> = _favorites.asStateFlow()

    private var allExercises: List<Exercise> = emptyList()
    private var currentLanguage: String = "Español"
    private var hasLoadedOnce = false

    init {
        loadFavorites()
        // No llamamos a loadExercises aquí para esperar a que el NavGraph sincronice el idioma real del DataStore
    }

    /**
     * Sincroniza el idioma desde el DataStore. 
     * Se llama desde el NavGraph al iniciar la app.
     */
    fun setLanguage(language: String) {
        if (currentLanguage != language || !hasLoadedOnce) {
            currentLanguage = language
            loadExercises()
        }
    }

    fun loadExercises() {
        viewModelScope.launch {
            _uiState.value = ExerciseUiState.Loading
            android.util.Log.d("ExerciseViewModel", "Iniciando carga de ejercicios...")
            repository.getExercises(currentLanguage).collect { result ->
                hasLoadedOnce = true
                result.fold(
                    onSuccess = { exercises ->
                        android.util.Log.d("ExerciseViewModel", "Carga exitosa: ${exercises.size} ejercicios")
                        allExercises = exercises
                        filterExercises()
                    },
                    onFailure = { error ->
                        android.util.Log.e("ExerciseViewModel", "Fallo en carga: ${error.message}")
                        _uiState.value = ExerciseUiState.Error(error.message ?: "Error de conexión con Supabase")
                    }
                )
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavorites().collect { favs ->
                _favorites.value = favs
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        filterExercises()
    }

    fun onCategoryChange(category: String) {
        _selectedCategory.value = category
        filterExercises()
    }

    private fun filterExercises() {
        val query = _searchQuery.value
        val category = _selectedCategory.value

        val filtered = allExercises.filter { exercise ->
            val matchesSearch = exercise.name.contains(query, ignoreCase = true) ||
                    exercise.description.contains(query, ignoreCase = true) ||
                    exercise.targetMuscle.contains(query, ignoreCase = true)
            
            val isAll = category == "Todos" || category == "All"
            val matchesCategory = isAll || 
                    exercise.targetMuscle.equals(category, ignoreCase = true) ||
                    mapMuscleToInternal(exercise.targetMuscle).equals(mapMuscleToInternal(category), ignoreCase = true)
            
            matchesSearch && matchesCategory
        }
        _uiState.value = ExerciseUiState.Success(filtered)
    }

    private fun mapMuscleToInternal(muscle: String): String {
        return when(muscle.lowercase()) {
            "pecho", "chest" -> "chest"
            "espalda", "back" -> "back"
            "piernas", "legs" -> "legs"
            "hombros", "shoulders" -> "shoulders"
            "brazos", "arms" -> "arms"
            else -> muscle.lowercase()
        }
    }

    fun toggleFavorite(exercise: Exercise) {
        viewModelScope.launch {
            repository.toggleFavorite(exercise)
        }
    }
}

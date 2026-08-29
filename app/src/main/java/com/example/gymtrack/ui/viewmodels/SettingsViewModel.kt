package com.example.gymtrack.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtrack.data.local.SettingsManager
import com.example.gymtrack.data.local.UserSettings
import com.example.gymtrack.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsManager: SettingsManager,
    private val userRepository: UserRepository
) : ViewModel() {

    val settings: StateFlow<UserSettings> = settingsManager.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSettings(true, "Español", true, "Lb", "", "Gym Enthusiast", "enthusiast@gymtrack.com")
        )

    private val _progressPhotos = MutableStateFlow<List<String>>(emptyList())
    val progressPhotos: StateFlow<List<String>> = _progressPhotos.asStateFlow()

    init {
        loadProgressPhotos()
    }

    fun loadProgressPhotos() {
        viewModelScope.launch {
            val result = userRepository.getProgressImages("user_default")
            result.onSuccess { _progressPhotos.value = it }
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch { settingsManager.saveDarkMode(enabled) }
    }

    fun updateLanguage(language: String) {
        viewModelScope.launch { settingsManager.saveLanguage(language) }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch { settingsManager.saveNotifications(enabled) }
    }

    fun updateWeightUnit(unit: String) {
        viewModelScope.launch { settingsManager.saveWeightUnit(unit) }
    }

    fun uploadProfilePhoto(bitmap: Bitmap, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val userId = "user_default" 
            val result = userRepository.uploadProfileImage(userId, bitmap)
            result.onSuccess { url ->
                settingsManager.saveProfileImageUrl(url)
                onResult(true, null)
            }.onFailure { error ->
                onResult(false, error.message)
            }
        }
    }

    fun uploadProgressPhoto(bitmap: Bitmap, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val userId = "user_default"
            val result = userRepository.uploadProgressImage(userId, bitmap)
            result.onSuccess { url ->
                // Guardar localmente o en tabla si existe
                onResult(true, null)
            }.onFailure { error ->
                onResult(false, error.message)
            }
        }
    }
}

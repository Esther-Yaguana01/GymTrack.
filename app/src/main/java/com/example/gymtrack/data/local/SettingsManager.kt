package com.example.gymtrack.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {
    companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val NOTIFICATIONS = booleanPreferencesKey("notifications")
        val WEIGHT_UNIT = stringPreferencesKey("weight_unit")
        val PROFILE_IMAGE_URL = stringPreferencesKey("profile_image_url")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    val settingsFlow: Flow<UserSettings> = context.dataStore.data.map { pref ->
        UserSettings(
            isDarkMode = pref[DARK_MODE] ?: true,
            language = pref[LANGUAGE] ?: "Español",
            isNotificationsEnabled = pref[NOTIFICATIONS] ?: true,
            weightUnit = pref[WEIGHT_UNIT] ?: "Lb",
            profileImageUrl = pref[PROFILE_IMAGE_URL] ?: "",
            userName = pref[USER_NAME] ?: "Gym Enthusiast",
            userEmail = pref[USER_EMAIL] ?: "enthusiast@gymtrack.com"
        )
    }

    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }

    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { it[LANGUAGE] = language }
    }

    suspend fun saveNotifications(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS] = enabled }
    }

    suspend fun saveWeightUnit(unit: String) {
        context.dataStore.edit { it[WEIGHT_UNIT] = unit }
    }

    suspend fun saveProfileImageUrl(url: String) {
        context.dataStore.edit { it[PROFILE_IMAGE_URL] = url }
    }
}

data class UserSettings(
    val isDarkMode: Boolean,
    val language: String,
    val isNotificationsEnabled: Boolean,
    val weightUnit: String,
    val profileImageUrl: String,
    val userName: String,
    val userEmail: String
)

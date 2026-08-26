package com.example.gymtrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val target: String,
    val gifUrl: String?,
    val difficulty: String?,
    val isFavorite: Boolean = false // Esta variable te servirá para la SCREEN 3 de favoritos
)
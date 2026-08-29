package com.example.gymtrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gymtrack.model.Exercise

@Entity(tableName = "favorites")
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val targetMuscle: String,
    val imageUrl: String,
    val equipment: String,
    val difficulty: String,
    val sets: String,
    val reps: String,
    val rest: String,
    val calories: String,
    val description: String,
    val duration: String,
    val weightKg: Double
) {
    fun toDomain(): Exercise {
        return Exercise(
            id = id,
            name = name,
            targetMuscle = targetMuscle,
            imageUrl = resolveImageUrl(id, imageUrl, name),
            equipment = equipment,
            difficulty = difficulty,
            sets = sets,
            reps = reps,
            rest = rest,
            calories = calories,
            description = description,
            duration = duration,
            weightKg = weightKg
        )
    }

    /**
     * Resolve una URL de imagen canónica por id de ejercicio o por nombre. Si la entidad ya tiene una URL válida
     * se usa como fallback. De lo contrario generamos una búsqueda en Unsplash por el nombre del ejercicio.
     */
    private fun resolveImageUrl(id: String, current: String, name: String): String {
        return try {
            val query = java.net.URLEncoder.encode(name.lowercase(), "UTF-8")
            "https://source.unsplash.com/1200x800/?$query,fitness&sig=${java.net.URLEncoder.encode(id, "UTF-8") }"
        } catch (e: Exception) {
            current.ifBlank { "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=1200&auto=format&fit=crop" }
        }
    }    companion object {
        fun fromDomain(exercise: Exercise): ExerciseEntity {
            return ExerciseEntity(
                id = exercise.id,
                name = exercise.name,
                targetMuscle = exercise.targetMuscle,
                imageUrl = exercise.imageUrl,
                equipment = exercise.equipment,
                difficulty = exercise.difficulty,
                sets = exercise.sets,
                reps = exercise.reps,
                rest = exercise.rest,
                calories = exercise.calories,
                description = exercise.description,
                duration = exercise.duration,
                weightKg = exercise.weightKg
            )
        }
    }
}

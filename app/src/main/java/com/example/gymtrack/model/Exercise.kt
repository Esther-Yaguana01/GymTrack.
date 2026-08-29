package com.example.gymtrack.model

/**
 * Modelo de dominio para un Ejercicio.
 * @param weightKg El peso base siempre se almacena en Kilogramos para consistencia.
 */
data class Exercise(
    val id: String,
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
    /**
     * Devuelve el peso convertido a la unidad deseada.
     */
    fun getDisplayWeight(unit: String): Double {
        return if (unit.lowercase() == "lb") {
            weightKg * 2.20462
        } else {
            weightKg
        }
    }
}

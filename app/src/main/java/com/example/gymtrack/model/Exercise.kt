package com.example.gymtrack.model

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
    val duration: String = "25 Min",
    val weight: String = "200 Lb"
)

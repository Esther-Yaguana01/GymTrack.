package com.example.gymtrack.data.repository

import com.example.gymtrack.data.remote.ExerciseApi
import com.example.gymtrack.data.local.ExerciseDao

class ExerciseRepository(
    private val api: ExerciseApi,
    private val dao: ExerciseDao
) {
    // Implementación del repositorio
}

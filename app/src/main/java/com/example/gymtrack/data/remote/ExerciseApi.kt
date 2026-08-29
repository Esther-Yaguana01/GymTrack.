package com.example.gymtrack.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface ExerciseApi {
    @GET("exercises?select=*")
    suspend fun getExercises(): Response<List<ExerciseDto>>
}

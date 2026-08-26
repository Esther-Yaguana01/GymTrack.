package com.example.gymtrack.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers

interface ExerciseApi {

    @Headers(
        "apikey: sb_publishable_pahKQgbJPA_sSM5HiKXGng_ks1KXqBv"
    )
    @GET("exercises")
    suspend fun getExercises(): List<ExerciseDto>
}
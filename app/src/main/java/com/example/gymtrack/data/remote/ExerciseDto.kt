package com.example.gymtrack.data.remote

import com.google.gson.annotations.SerializedName

data class ExerciseDto(
    val id: Long,
    val name: String,

    @SerializedName("body_part")
    val bodyPart: String,

    val equipment: String,
    val target: String,

    @SerializedName("gif_url")
    val gifUrl: String?,

    val difficulty: String?,
    val description: String?,
    val sets: Int?,
    val repetitions: String?,

    @SerializedName("rest_time")
    val restTime: Int?
)
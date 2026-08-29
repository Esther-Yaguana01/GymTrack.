package com.example.gymtrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExerciseEntity::class, ProgressEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
}

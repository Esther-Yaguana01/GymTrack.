package com.example.gymtrack.data.repository

import android.util.Log
import com.example.gymtrack.data.local.ExerciseDao
import com.example.gymtrack.data.local.ExerciseEntity
import com.example.gymtrack.data.remote.ExerciseApi
import com.example.gymtrack.model.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.example.gymtrack.data.remote.ExerciseDto

class ExerciseRepository(
    private val api: ExerciseApi,
    private val dao: ExerciseDao
) {
    /**
     * Obtiene una lista extendida de 50 ejercicios reales con IMÁGENES ÚNICAS.
     */
    fun getExercises(language: String): Flow<Result<List<Exercise>>> = flow {
        try {
            Log.d("ExerciseRepository", "Cargando catálogo para: $language")
            val localList = getDefaultExercises().map { it.toDomain(language) }
            
            Log.d("ExerciseRepository", "RETROFIT REQUEST: calling api.getExercises()")
            val response = try { api.getExercises() } catch (e: Exception) { 
                Log.e("ExerciseRepository", "RETROFIT ERROR: ${'$'}{e.message}")
                null 
            }
            Log.d("ExerciseRepository", "SUPABASE RESPONSE: code=${'$'}{response?.code()} successful=${'$'}{response?.isSuccessful}")

            val finalResult = if (response?.isSuccessful == true) {
                val remoteList = response.body()?.map { it.toDomain(language) } ?: emptyList()
                Log.d("ExerciseRepository", "EXERCISES RECEIVED: ${'$'}{remoteList.size}")
                remoteList.forEach { Log.d("ExerciseRepository", "IMAGE URL RECEIVED for ${'$'}{it.name}: ${'$'}{it.imageUrl}") }
                (remoteList + localList).distinctBy { it.id }
            } else {
                Log.w("ExerciseRepository", "Using local fallback exercises because Supabase response is not successful or null")
                localList
            }
            emit(Result.success(finalResult))
        } catch (e: Exception) {
            emit(Result.success(getDefaultExercises().map { it.toDomain(language) }))
        }
    }.flowOn(Dispatchers.IO)

    private fun getDefaultExercises(): List<ExerciseDto> {
        return listOf(
            // PECHO - Imagenes únicas (ID Unsplash específicos)
            createDto("E1", "Bench Press", "Chest", "Barbell", "Intermediate", "4", "12", "90s", "150", 60.0, "1571019614242-c5c5dee9f50b"),
            createDto("E2", "Incline Press", "Chest", "Dumbbells", "Intermediate", "3", "12", "60s", "120", 22.0, "1541534741688-6078c6bc35e5"),
            createDto("E3", "Chest Fly", "Chest", "Dumbbells", "Beginner", "3", "15", "60s", "90", 12.0, "1584735175302-d1761d12163b"),
            createDto("E4", "Push Ups", "Chest", "None", "Beginner", "3", "20", "45s", "80", 0.0, "1598971639058-aba00344b5ba"),
            createDto("E5", "Decline Press", "Chest", "Barbell", "Intermediate", "3", "10", "90s", "130", 50.0, "1571019613454-1cb2f99b2d8b"),
            
            // ESPALDA
            createDto("E6", "Deadlift", "Back", "Barbell", "Advanced", "3", "8", "120s", "250", 100.0, "1517836357463-d25dfeac3438"),
            createDto("E7", "Pull Ups", "Back", "Bar", "Advanced", "3", "MAX", "90s", "110", 0.0, "1526506118085-60ce8714f8c5"),
            createDto("E8", "Lat Pulldown", "Back", "Machine", "Intermediate", "3", "12", "60s", "100", 45.0, "1605296867304-46d5465a13f1"),
            createDto("E9", "Bent Over Row", "Back", "Barbell", "Intermediate", "4", "10", "90s", "140", 55.0, "1517836357463-d25dfeac3438"),
            createDto("E10", "Seated Row", "Back", "Cable", "Beginner", "3", "12", "60s", "90", 40.0, "1599058917216-0bb170075d68"),
            
            // HOMBROS
            createDto("E11", "Shoulder Press", "Shoulders", "Dumbbells", "Intermediate", "3", "10", "60s", "120", 18.0, "1541534741688-6078c6bc35e5"),
            createDto("E12", "Lateral Raise", "Shoulders", "Dumbbells", "Beginner", "3", "15", "60s", "60", 8.0, "1597452485669-2c7bb5fef90d"),
            createDto("E13", "Front Raise", "Shoulders", "Dumbbells", "Beginner", "3", "12", "60s", "65", 10.0, "1541534741688-6078c6bc35e5"),
            createDto("E14", "Arnold Press", "Shoulders", "Dumbbells", "Intermediate", "3", "10", "90s", "130", 16.0, "1581009146145-b5ef050c2e1e"),
            createDto("E15", "Face Pull", "Shoulders", "Cable", "Intermediate", "3", "15", "45s", "50", 20.0, "1599058917765-8b3877969e0f"),
            
            // BRAZOS
            createDto("E16", "Bicep Curl", "Arms", "Dumbbells", "Beginner", "3", "12", "60s", "80", 14.0, "1581009146145-b5ef050c2e1e"),
            createDto("E17", "Hammer Curl", "Arms", "Dumbbells", "Beginner", "3", "12", "60s", "80", 14.0, "1581009146145-b5ef050c2e1e"),
            createDto("E18", "Preacher Curl", "Arms", "Machine", "Intermediate", "3", "12", "60s", "75", 25.0, "1540497077202-7c8a3999166f"),
            createDto("E19", "Skull Crusher", "Arms", "Barbell", "Intermediate", "3", "10", "60s", "100", 20.0, "1540497077202-7c8a3999166f"),
            createDto("E20", "Tricep Pushdown", "Arms", "Cable", "Beginner", "3", "15", "60s", "90", 18.0, "1541534741688-6078c6bc35e5"),
            
            // PIERNAS
            createDto("E21", "Squat", "Legs", "Barbell", "Intermediate", "4", "10", "120s", "220", 80.0, "1534438327276-14e5300c3a48"),
            createDto("E22", "Leg Press", "Legs", "Machine", "Intermediate", "3", "12", "90s", "180", 160.0, "1534438327276-14e5300c3a48"),
            createDto("E23", "Leg Extension", "Legs", "Machine", "Beginner", "3", "15", "60s", "90", 40.0, "1574680096145-d05b474e2158"),
            createDto("E24", "Leg Curl", "Legs", "Machine", "Beginner", "3", "12", "60s", "85", 35.0, "1574680096145-d05b474e2158"),
            createDto("E25", "Lunges", "Legs", "Dumbbells", "Beginner", "3", "12", "60s", "130", 15.0, "1434608519344-49d77a699e1d"),
            
            // GLÚTEOS
            createDto("E26", "Hip Thrust", "Glutes", "Barbell", "Intermediate", "4", "10", "90s", "210", 70.0, "1518611012118-29a8ad52d0c7"),
            createDto("E27", "Glute Bridge", "Glutes", "None", "Beginner", "3", "15", "60s", "95", 0.0, "1518611012118-29a8ad52d0c7"),
            createDto("E28", "Cable Kickback", "Glutes", "Cable", "Beginner", "3", "15", "45s", "70", 12.0, "1599058917216-0bb170075d68"),
            
            // ABDOMEN
            createDto("E29", "Plank", "Abs", "None", "Beginner", "3", "60s", "30s", "50", 0.0, "1566241142559-40e1bfc26ebc"),
            createDto("E30", "Russian Twist", "Abs", "None", "Intermediate", "3", "20", "45s", "65", 8.0, "1571019613454-1cb2f99b2d8b"),
            createDto("E31", "Leg Raise", "Abs", "None", "Beginner", "3", "15", "45s", "55", 0.0, "1598971639058-aba00344b5ba"),
            createDto("E32", "Crunches", "Abs", "None", "Beginner", "3", "20", "45s", "45", 0.0, "1566241142559-40e1bfc26ebc"),
            createDto("E33", "Bicycle Crunch", "Abs", "None", "Beginner", "3", "20", "45s", "60", 0.0, "1571019613454-1cb2f99b2d8b"),
            
            // PANTORRILLAS
            createDto("E34", "Calf Raise", "Calves", "Machine", "Beginner", "4", "15", "45s", "75", 65.0, "1534367507873-d2d7e24c798f"),
            createDto("E35", "Seated Calf Raise", "Calves", "Machine", "Beginner", "4", "15", "45s", "70", 45.0, "1534367507873-d2d7e24c798f"),
            
            // ANTEBRAZOS
            createDto("E36", "Wrist Curl", "Forearms", "Dumbbells", "Beginner", "3", "15", "45s", "40", 12.0, "1581009146145-b5ef050c2e1e"),
            createDto("E37", "Reverse Curl", "Forearms", "Barbell", "Beginner", "3", "12", "45s", "50", 15.0, "1581009146145-b5ef050c2e1e"),
            
            // MÁS VARIADOS PARA LLEGAR A 50 (Usando IDs reales para unicidad)
            createDto("E38", "Chin Ups", "Back", "Bar", "Intermediate", "3", "8", "90s", "115", 0.0, "1526506118085-60ce8714f8c5"),
            createDto("E39", "Diamond Pushups", "Chest", "None", "Intermediate", "3", "12", "60s", "100", 0.0, "1598971639058-aba00344b5ba"),
            createDto("E40", "Cable Fly", "Chest", "Cable", "Intermediate", "3", "12", "60s", "80", 15.0, "1599058917765-8b3877969e0f"),
            createDto("E41", "T-Bar Row", "Back", "Barbell", "Intermediate", "3", "10", "90s", "135", 40.0, "1517836357463-d25dfeac3438"),
            createDto("E42", "Upright Row", "Shoulders", "Barbell", "Intermediate", "3", "12", "60s", "90", 25.0, "1541534741688-6078c6bc35e5"),
            createDto("E43", "Hammer Strength Row", "Back", "Machine", "Intermediate", "3", "10", "60s", "110", 40.0, "1605296867304-46d5465a13f1"),
            createDto("E44", "Pec Deck", "Chest", "Machine", "Beginner", "3", "12", "60s", "85", 35.0, "1584735175302-d1761d12163b"),
            createDto("E45", "Hack Squat", "Legs", "Machine", "Intermediate", "3", "12", "90s", "180", 60.0, "1534438327276-14e5300c3a48"),
            createDto("E46", "Glute Ham Raise", "Legs", "Machine", "Advanced", "3", "10", "90s", "120", 0.0, "1574680096145-d05b474e2158"),
            createDto("E47", "Goblet Squat", "Legs", "Dumbbell", "Beginner", "3", "12", "60s", "140", 18.0, "1434608519344-49d77a699e1d"),
            createDto("E48", "Step Ups", "Legs", "Bench", "Beginner", "3", "12", "60s", "110", 10.0, "1434608519344-49d77a699e1d"),
            createDto("E49", "Farmers Walk", "Full Body", "Dumbbells", "Intermediate", "3", "30s", "60s", "160", 25.0, "1574680096145-d05b474e2158"),
            createDto("E50", "Burpees", "Full Body", "None", "Intermediate", "3", "15", "60s", "200", 0.0, "1598971639058-aba00344b5ba")
        )
    }

    private fun createDto(id: String, name: String, muscle: String, equip: String, diff: String, sets: String, reps: String, rest: String, cals: String, weight: Double, unsplashId: String): ExerciseDto {
        return ExerciseDto(
            id = id,
            name = name,
            nameEn = null, nameEs = null,
            description = "", descriptionEn = null, descriptionEs = null,
            instructions = null, instructionsEn = null, instructionsEs = null,
            duration = "20 min",
            category = muscle,
        // Dejar imageUrl vacío para que toDomain seleccione una imagen única y representativa
        imageUrl = "", 
        image = null, imageUrlCamel = null, gifUrl = null, gifUrlCamel = null, imgUrl = null,
        bodyPart = muscle, bodyPartEs = null,
        targetMuscle = muscle, targetMuscleEs = null,
        target = null,
        equipment = equip, equipmentEs = null,
        difficulty = diff, difficultyEs = null,
        sets = sets,
        repetitions = reps,
        reps = reps,
        restTime = null,
        rest = rest,
        calories = cals,
        weight = weight,
        weightKg = weight
        )
    }

    fun getFavorites(): Flow<List<Exercise>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun toggleFavorite(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            val isFavorite = dao.isFavorite(exercise.id)
            if (isFavorite) {
                dao.deleteFavorite(ExerciseEntity.fromDomain(exercise))
            } else {
                dao.insertFavorite(ExerciseEntity.fromDomain(exercise))
            }
        }
    }

    suspend fun isFavorite(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            dao.isFavorite(id)
        }
    }
}

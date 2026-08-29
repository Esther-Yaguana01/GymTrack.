package com.example.gymtrack.data.remote

import com.example.gymtrack.model.Exercise
import com.google.gson.annotations.SerializedName
import java.util.Locale
import com.example.gymtrack.data.remote.ExerciseImages

/**
 * Data Transfer Object con soporte multilingüe y mapeo flexible de columnas.
 */
data class ExerciseDto(
    @SerializedName("id") val id: Any,
    @SerializedName("name") val name: String?,
    @SerializedName("name_en") val nameEn: String?,
    @SerializedName("name_es") val nameEs: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("description_en") val descriptionEn: String?,
    @SerializedName("description_es") val descriptionEs: String?,
    @SerializedName("instructions") val instructions: String?,
    @SerializedName("instructions_en") val instructionsEn: String?,
    @SerializedName("instructions_es") val instructionsEs: String?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("imageUrl") val imageUrlCamel: String?,
    @SerializedName("gif_url") val gifUrl: String?,
    @SerializedName("gifUrl") val gifUrlCamel: String?,
    @SerializedName("img_url") val imgUrl: String?,
    @SerializedName("body_part") val bodyPart: String?,
    @SerializedName("body_part_es") val bodyPartEs: String?,
    @SerializedName("target_muscle") val targetMuscle: String?,
    @SerializedName("target_muscle_es") val targetMuscleEs: String?,
    @SerializedName("target") val target: String?,
    @SerializedName("equipment") val equipment: String?,
    @SerializedName("equipment_es") val equipmentEs: String?,
    @SerializedName("difficulty") val difficulty: String?,
    @SerializedName("difficulty_es") val difficultyEs: String?,
    @SerializedName("sets") val sets: Any?,
    @SerializedName("repetitions") val repetitions: String?,
    @SerializedName("reps") val reps: String?,
    @SerializedName("rest_time") val restTime: Any?,
    @SerializedName("rest") val rest: String?,
    @SerializedName("calories") val calories: String?,
    @SerializedName("weight") val weight: Any?,
    @SerializedName("weight_kg") val weightKg: Any?
) {
    fun toDomain(language: String = "Español"): Exercise {
        val lower = language.lowercase()
        val isSpanish = when {
            lower.contains("esp") || lower.contains("espa") || lower.contains("es") -> true
            lower.contains("eng") || lower.contains("en") || lower.contains("english") -> false
            else -> true
        }
        
        // 1. Nombre Traducido (Con fallback sistemático)
        val rawName = nameEs ?: name ?: nameEn ?: "Ejercicio"
        val finalName = if (isSpanish) translateName(rawName, true) else translateName(rawName, false)
        
        // 2. Descripción Traducida
        val rawDesc = descriptionEs ?: description ?: descriptionEn ?: ""
        val finalDescription = translateDescription(rawDesc, finalName, isSpanish)

        // 3. Músculo Traducido
        val rawMuscle = targetMuscleEs ?: bodyPartEs ?: targetMuscle ?: bodyPart ?: category ?: "General"
        val finalMuscle = if (isSpanish) translateMuscle(rawMuscle, true) else translateMuscle(rawMuscle, false)

        // 4. Equipamiento Traducido
        val rawEquip = equipmentEs ?: equipment ?: "No requiere equipo"
        val finalEquipment = if (isSpanish) translateEquipment(rawEquip, true) else translateEquipment(rawEquip, false)
                             
        // 5. Dificultad
        val rawDiff = difficultyEs ?: difficulty ?: "Intermedio"
        val finalDifficulty = if (isSpanish) translateDifficulty(rawDiff, true) else translateDifficulty(rawDiff, false)

        // 6. Imagen Real ÚNICA: priorizar URLs provistas por la API; si faltan, usar catálogo local estable (ExerciseImages)
        var bestImageUrl = imageUrl ?: image ?: imageUrlCamel ?: gifUrl ?: gifUrlCamel ?: imgUrl ?: ""
        if (bestImageUrl.isEmpty() || bestImageUrl.contains("placeholder")) {
            bestImageUrl = getUniqueImageForExercise(id.toString(), finalName)
        }
        
        val rawWeight = weightKg ?: weight ?: 0.0
        val numericWeight = when(rawWeight) {
            is Number -> rawWeight.toDouble()
            is String -> rawWeight.toDoubleOrNull() ?: 0.0
            else -> 0.0
        }
        
        return Exercise(
            id = id.toString(),
            name = finalName,
            targetMuscle = finalMuscle,
            imageUrl = bestImageUrl,
            equipment = finalEquipment,
            difficulty = finalDifficulty,
            sets = sets?.toString() ?: "4",
            reps = reps ?: repetitions ?: "12",
            rest = rest ?: restTime?.toString() ?: "60s",
            calories = calories ?: "100 kcal",
            description = finalDescription,
            duration = duration ?: "15 min",
            weightKg = numericWeight
        )
    }

    private fun translateName(name: String, toSpanish: Boolean): String {
        val mapping = mapOf(
            "bench press" to Pair("Press de banca", "Bench Press"),
            "barbell squat" to Pair("Sentadilla con barra", "Barbell Squat"),
            "deadlift" to Pair("Peso muerto", "Deadlift"),
            "bicep curl" to Pair("Curl de bíceps", "Bicep Curl"),
            "shoulder press" to Pair("Press militar", "Shoulder Press"),
            "pull up" to Pair("Dominadas", "Pull Ups"),
            "chin up" to Pair("Dominadas supinas", "Chin Ups"),
            "lunges" to Pair("Zancadas", "Lunges"),
            "plank" to Pair("Plancha", "Plank"),
            "push up" to Pair("Flexiones", "Push Ups"),
            "leg press" to Pair("Prensa de piernas", "Leg Press"),
            "tricep extension" to Pair("Extensión de tríceps", "Tricep Extension"),
            "hammer curl" to Pair("Curl martillo", "Hammer Curl"),
            "lat pulldown" to Pair("Jalón al pecho", "Lat Pulldown"),
            "chest fly" to Pair("Aperturas de pecho", "Chest Fly"),
            "row" to Pair("Remo", "Row"),
            "calf raise" to Pair("Elevación de talones", "Calf Raise"),
            "lateral raise" to Pair("Elevación lateral", "Lateral Raise"),
            "dips" to Pair("Fondos", "Dips"),
            "leg curl" to Pair("Curl de pierna", "Leg Curl"),
            "face pull" to Pair("Face Pull", "Face Pull"),
            "incline press" to Pair("Press inclinado", "Incline Press"),
            "glute bridge" to Pair("Puente de glúteo", "Glute Bridge"),
            "mountain climber" to Pair("Escaladores", "Mountain Climber"),
            "skull crusher" to Pair("Press francés", "Skull Crusher"),
            "arnold press" to Pair("Press Arnold", "Arnold Press"),
            "romanian deadlift" to Pair("Peso muerto rumano", "Romanian Deadlift"),
            "preacher curl" to Pair("Curl predicador", "Preacher Curl"),
            "leg extension" to Pair("Extensión de pierna", "Leg Extension"),
            "bent over row" to Pair("Remo inclinado", "Bent Over Row"),
            "seated row" to Pair("Remo en polea baja", "Seated Row"),
            "overhead extension" to Pair("Extensión tras nuca", "Overhead Extension"),
            "hack squat" to Pair("Sentadilla Hack", "Hack Squat"),
            "hip thrust" to Pair("Hip Thrust", "Hip Thrust"),
            "wrist curl" to Pair("Curl de muñeca", "Wrist Curl"),
            "reverse curl" to Pair("Curl inverso", "Reverse Curl"),
            "decline press" to Pair("Press declinado", "Decline Press"),
            "one arm row" to Pair("Remo a una mano", "One Arm Row"),
            "rear delt fly" to Pair("Pájaro", "Rear Delt Fly"),
            "cable pushdown" to Pair("Extensión en polea", "Cable Pushdown"),
            "stiff leg deadlift" to Pair("Peso muerto piernas rígidas", "Stiff Leg Deadlift"),
            "kickback" to Pair("Patada de tríceps", "Kickback"),
            "bicycle crunch" to Pair("Crunch de bicicleta", "Bicycle Crunch"),
            "jump squat" to Pair("Sentadilla con salto", "Jump Squat"),
            "farmers walk" to Pair("Paseo del granjero", "Farmers Walk"),
            "burpee" to Pair("Burpees", "Burpees"),
            "jumping jack" to Pair("Jumping Jacks", "Jumping Jacks"),
            "step up" to Pair("Step Up", "Step Up"),
            "battle rope" to Pair("Cuerdas de batalla", "Battle Ropes"),
            "goblet squat" to Pair("Sentadilla Goblet", "Goblet Squat")
        )
        val key = name.lowercase()
        val entry = mapping.entries.find { key.contains(it.key) }
        return if (entry != null) {
            if (toSpanish) entry.value.first else entry.value.second
        } else name
    }

    private fun translateMuscle(muscle: String, toSpanish: Boolean): String {
        val mapping = mapOf(
            "chest" to Pair("Pecho", "Chest"),
            "back" to Pair("Espalda", "Back"),
            "legs" to Pair("Piernas", "Legs"),
            "quads" to Pair("Cuádriceps", "Quads"),
            "hamstrings" to Pair("Isquiotibiales", "Hamstrings"),
            "shoulders" to Pair("Hombros", "Shoulders"),
            "arms" to Pair("Brazos", "Arms"),
            "biceps" to Pair("Bíceps", "Biceps"),
            "triceps" to Pair("Tríceps", "Triceps"),
            "abs" to Pair("Abdomen", "Abs"),
            "glutes" to Pair("Glúteos", "Glutes"),
            "calves" to Pair("Pantorrillas", "Calves"),
            "forearms" to Pair("Antebrazos", "Forearms")
        )
        val key = muscle.lowercase()
        val entry = mapping[key] ?: mapping.entries.find { key.contains(it.key) }?.value
        return if (entry != null) {
            if (toSpanish) entry.first else entry.second
        } else muscle
    }

    private fun translateEquipment(equip: String, toSpanish: Boolean): String {
        val mapping = mapOf(
            "barbell" to Pair("Barra", "Barbell"),
            "dumbbell" to Pair("Mancuernas", "Dumbbells"),
            "machine" to Pair("Máquina", "Machine"),
            "none" to Pair("Ninguno", "None"),
            "bodyweight" to Pair("Peso corporal", "Bodyweight"),
            "kettlebell" to Pair("Pesa rusa", "Kettlebell"),
            "bench" to Pair("Banco", "Bench"),
            "cable" to Pair("Polea", "Cable"),
            "bars" to Pair("Barras", "Bars")
        )
        val key = equip.lowercase()
        val entry = mapping.entries.find { key.contains(it.key) }
        return if (entry != null) {
            if (toSpanish) entry.value.first else entry.value.second
        } else equip
    }

    private fun translateDifficulty(diff: String, toSpanish: Boolean): String {
        val mapping = mapOf(
            "beginner" to Pair("Principiante", "Beginner"),
            "intermediate" to Pair("Intermedio", "Intermediate"),
            "advanced" to Pair("Avanzado", "Advanced")
        )
        val key = diff.lowercase()
        val entry = mapping[key] ?: mapping.entries.find { key.contains(it.key) }?.value
        return if (entry != null) {
            if (toSpanish) entry.first else entry.second
        } else if (toSpanish) "Intermedio" else "Intermediate"
    }

    private fun translateDescription(desc: String, name: String, toSpanish: Boolean): String {
        if (desc.length > 50) return desc
        val instructions = mapOf(
            "Press de banca" to Pair("Túmbate en el banco, baja la barra al pecho y empuja con fuerza hacia arriba.", "Lie on the bench, lower the bar to your chest and push up with power."),
            "Sentadilla" to Pair("Coloca la barra en los hombros, baja la cadera rompiendo el paralelo y sube.", "Place the bar on shoulders, lower your hips past parallel and stand up."),
            "Peso muerto" to Pair("Levanta la barra desde el suelo manteniendo la espalda recta hasta quedar erguido.", "Lift the bar from the floor keeping your back straight until standing upright."),
            "Curl de bíceps" to Pair("Sujeta las mancuernas y flexiona los codos sin mover el torso.", "Hold the dumbbells and flex your elbows without moving your torso."),
            "Press militar" to Pair("Empuja la barra sobre la cabeza desde la parte superior del pecho.", "Push the bar overhead from the upper chest position."),
            "Dominada" to Pair("Cuélgate de la barra y eleva tu cuerpo hasta que la barbilla pase la barra.", "Hang from the bar and pull your body up until your chin clears the bar."),
            "Zancada" to Pair("Da un paso largo hacia adelante y baja la rodilla trasera hacia el suelo.", "Take a long step forward and lower your back knee toward the floor."),
            "Plancha" to Pair("Mantén una posición de flexión pero apoyado en los antebrazos con el core firme.", "Hold a push-up position but supported on your forearms with a tight core."),
            "Prensa" to Pair("Empuja la plataforma con las piernas hasta casi extenderlas por completo.", "Push the platform with your legs until they are almost fully extended."),
            "Remo" to Pair("Inclina el torso y tira de la barra o mancuerna hacia tu ombligo.", "Lean your torso and pull the bar or dumbbell toward your navel."),
            "Elevación lateral" to Pair("Eleva las mancuernas hacia los lados hasta la altura de los hombros.", "Raise the dumbbells to the sides up to shoulder height."),
            "Fondo" to Pair("Baja el cuerpo entre las barras paralelas flexionando los codos a 90 grados.", "Lower your body between parallel bars by flexing elbows to 90 degrees."),
            "Extensión" to Pair("Extiende completamente la articulación para aislar el músculo trabajado.", "Fully extend the joint to isolate the targeted muscle."),
            "Puente de glúteo" to Pair("Tumbado boca arriba, eleva la cadera contrayendo los glúteos.", "Lying on your back, raise your hips by contracting your glutes.")
        )
        val entry = instructions.entries.find { name.lowercase().contains(it.key.lowercase()) }?.value
        return if (entry != null) {
            if (toSpanish) entry.first else entry.second
        } else if (desc.isEmpty()) {
            if (toSpanish) "Realiza el movimiento de forma controlada y con buena técnica." else "Perform the movement in a controlled manner with good technique."
        } else desc
    }

    /**
     * Devuelve una imagen ÚNICA para cada ejercicio usando IDs específicos de Unsplash.
     */
    private fun getUniqueImageForExercise(id: String, name: String): String {
        // Usar catálogo local de URLs directas (ExerciseImages). Evitar búsquedas dinámicas.
        return try {
            ExerciseImages.getImageFor(id, name)
        } catch (e: Exception) {
            "https://images.unsplash.com/photo-1554284126-aa88f22d8c76?w=1200&auto=format&fit=crop&q=80"
        }
    }
}

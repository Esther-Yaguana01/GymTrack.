package com.example.gymtrack.data.remote

/**
 * Catálogo local de imágenes directas (URLs estables) para ejercicios.
 * Las claves son palabras clave (en minúsculas) que pueden aparecer en el nombre final del ejercicio.
 * Si no se encuentra una coincidencia, se devuelve una imagen por defecto.
 */
object ExerciseImages {
    // Mapeo por id (E1..E50) y por nombre clave para fallback.
    private val idMap = mapOf(
        "E1" to "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?w=1200&auto=format&fit=crop&q=80",    // Bench Press
        "E2" to "https://images.unsplash.com/photo-1541534741688-6078c6bc35e5?w=1200&auto=format&fit=crop&q=80",    // Incline Press
        "E3" to "https://images.unsplash.com/photo-1584735175302-d1761d12163b?w=1200&auto=format&fit=crop&q=80",    // Chest Fly
        "E4" to "https://images.unsplash.com/photo-1598971639058-aba00344b5ba?w=1200&auto=format&fit=crop&q=80",    // Push Ups
        "E5" to "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=1200&auto=format&fit=crop&q=80",    // Decline Press

        "E6" to "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=1200&auto=format&fit=crop&q=80",    // Deadlift
        "E7" to "https://images.unsplash.com/photo-1526506118085-60ce8714f8c5?w=1200&auto=format&fit=crop&q=80",    // Pull Ups
        "E8" to "https://images.unsplash.com/photo-1605296867304-46d5465a13f1?w=1200&auto=format&fit=crop&q=80",    // Lat Pulldown
        "E9" to "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=1200&auto=format&fit=crop&q=80",    // Bent Over Row
        "E10" to "https://images.unsplash.com/photo-1599058917216-0bb170075d68?w=1200&auto=format&fit=crop&q=80",   // Seated Row

        "E11" to "https://images.unsplash.com/photo-1541534741688-6078c6bc35e5?w=1200&auto=format&fit=crop&q=80",   // Shoulder Press
        "E12" to "https://images.unsplash.com/photo-1597452485669-2c7bb5fef90d?w=1200&auto=format&fit=crop&q=80",   // Lateral Raise
        "E13" to "https://images.unsplash.com/photo-1541534741688-6078c6bc35e5?w=1200&auto=format&fit=crop&q=80",   // Front Raise
        "E14" to "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=1200&auto=format&fit=crop&q=80",   // Arnold Press
        "E15" to "https://images.unsplash.com/photo-1599058917775-8b3877969e0f?w=1200&auto=format&fit=crop&q=80",   // Face Pull

        "E16" to "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=1200&auto=format&fit=crop&q=80",   // Bicep Curl
        "E17" to "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=1200&auto=format&fit=crop&q=80",   // Hammer Curl
        "E18" to "https://images.unsplash.com/photo-1540497077202-7c8a3999166f?w=1200&auto=format&fit=crop&q=80",   // Preacher Curl
        "E19" to "https://images.unsplash.com/photo-1540497077202-7c8a3999166f?w=1200&auto=format&fit=crop&q=80",   // Skull Crusher
        "E20" to "https://images.unsplash.com/photo-1541534741688-6078c6bc35e5?w=1200&auto=format&fit=crop&q=80",   // Tricep Pushdown

        "E21" to "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=1200&auto=format&fit=crop&q=80",   // Squat
        "E22" to "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=1200&auto=format&fit=crop&q=80",   // Leg Press
        "E23" to "https://images.unsplash.com/photo-1574680096145-d05b474e2158?w=1200&auto=format&fit=crop&q=80",   // Leg Extension
        "E24" to "https://images.unsplash.com/photo-1574680096145-d05b474e2158?w=1200&auto=format&fit=crop&q=80",   // Leg Curl
        "E25" to "https://images.unsplash.com/photo-1434608519344-49d77a699e1d?w=1200&auto=format&fit=crop&q=80",   // Lunges

        "E26" to "https://images.unsplash.com/photo-1518611012118-29a8ad52d0c7?w=1200&auto=format&fit=crop&q=80",   // Hip Thrust
        "E27" to "https://images.unsplash.com/photo-1518611012118-29a8ad52d0c7?w=1200&auto=format&fit=crop&q=80",   // Glute Bridge
        "E28" to "https://images.unsplash.com/photo-1599058917216-0bb170075d68?w=1200&auto=format&fit=crop&q=80",   // Cable Kickback
        "E29" to "https://images.unsplash.com/photo-1566241142559-40e1bfc26ebc?w=1200&auto=format&fit=crop&q=80",   // Plank
        "E30" to "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=1200&auto=format&fit=crop&q=80",   // Russian Twist

        "E31" to "https://images.unsplash.com/photo-1598971639058-aba00344b5ba?w=1200&auto=format&fit=crop&q=80",   // Leg Raise
        "E32" to "https://images.unsplash.com/photo-1566241142559-40e1bfc26ebc?w=1200&auto=format&fit=crop&q=80",   // Crunches
        "E33" to "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=1200&auto=format&fit=crop&q=80",   // Bicycle Crunch
        "E34" to "https://images.unsplash.com/photo-1534367507873-d2d7e24c798f?w=1200&auto=format&fit=crop&q=80",   // Calf Raise
        "E35" to "https://images.unsplash.com/photo-1534367507873-d2d7e24c798f?w=1200&auto=format&fit=crop&q=80",   // Seated Calf Raise

        "E36" to "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=1200&auto=format&fit=crop&q=80",   // Wrist Curl
        "E37" to "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=1200&auto=format&fit=crop&q=80",   // Reverse Curl
        "E38" to "https://images.unsplash.com/photo-1526506118085-60ce8714f8c5?w=1200&auto=format&fit=crop&q=80",   // Chin Ups
        "E39" to "https://images.unsplash.com/photo-1598971639058-aba00344b5ba?w=1200&auto=format&fit=crop&q=80",   // Diamond Pushups
        "E40" to "https://images.unsplash.com/photo-1599058917775-8b3877969e0f?w=1200&auto=format&fit=crop&q=80",   // Cable Fly

        "E41" to "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=1200&auto=format&fit=crop&q=80",   // T-Bar Row
        "E42" to "https://images.unsplash.com/photo-1541534741688-6078c6bc35e5?w=1200&auto=format&fit=crop&q=80",   // Upright Row
        "E43" to "https://images.unsplash.com/photo-1605296867304-46d5465a13f1?w=1200&auto=format&fit=crop&q=80",   // Hammer Strength Row
        "E44" to "https://images.unsplash.com/photo-1584735175302-d1761d12163b?w=1200&auto=format&fit=crop&q=80",   // Pec Deck
        "E45" to "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=1200&auto=format&fit=crop&q=80",   // Hack Squat

        "E46" to "https://images.unsplash.com/photo-1574680096145-d05b474e2158?w=1200&auto=format&fit=crop&q=80",   // Glute Ham Raise
        "E47" to "https://images.unsplash.com/photo-1434608519344-49d77a699e1d?w=1200&auto=format&fit=crop&q=80",   // Goblet Squat
        "E48" to "https://images.unsplash.com/photo-1434608519344-49d77a699e1d?w=1200&auto=format&fit=crop&q=80",   // Step Ups
        "E49" to "https://images.unsplash.com/photo-1574680096145-d05b474e2158?w=1200&auto=format&fit=crop&q=80",   // Farmers Walk
        "E50" to "https://images.unsplash.com/photo-1598971639058-aba00344b5ba?w=1200&auto=format&fit=crop&q=80"    // Burpees
    )

    // Mapeo por palabras clave en nombre (inglés y español) para ejercicios remotos sin id conocido
    private val nameMap = mapOf(
        "bench press" to idMap["E1"]!!,
        "press de banca" to idMap["E1"]!!,
        "incline press" to idMap["E2"]!!,
        "chest fly" to idMap["E3"]!!,
        "push ups" to idMap["E4"]!!,
        "flexiones" to idMap["E4"]!!,
        "decline press" to idMap["E5"]!!,
        "deadlift" to idMap["E6"]!!,
        "peso muerto" to idMap["E6"]!!,
        "pull up" to idMap["E7"]!!,
        "dominadas" to idMap["E7"]!!,
        "lat pulldown" to idMap["E8"]!!,
        "bicep curl" to idMap["E16"]!!,
        "curl de bíceps" to idMap["E16"]!!,
        "squat" to idMap["E21"]!!,
        "sentadilla" to idMap["E21"]!!
    )

    fun getImageFor(id: String, name: String): String {
        // 1) buscar por id exacto
        idMap[id]?.let { return it }

        // 2) buscar por coincidencia en nombre (minúsculas)
        val key = name.lowercase()
        nameMap.forEach { (k, v) -> if (key.contains(k)) return v }

        // 3) default (usar E1)
        return idMap["E1"]!!
    }
}

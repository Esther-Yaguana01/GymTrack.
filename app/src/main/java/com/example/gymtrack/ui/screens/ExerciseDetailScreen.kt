package com.example.gymtrack.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gymtrack.model.Exercise

@Composable
fun ExerciseDetailScreen(
    exercise: Exercise,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val electricBlue = Color(0xFF2196F3) // Azul Eléctrico para la cámara
    
    // --- Lógica Real de la Cámara ---
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            Toast.makeText(context, "¡Foto capturada con éxito!", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // --- Mitad Superior (Imagen HD) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
        ) {
            AsyncImage(
                model = exercise.imageUrl,
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Botón Volver
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }

            // Botón de Cámara Flotante (Azul Eléctrico)
            FloatingActionButton(
                onClick = { cameraLauncher.launch() }, // Abre la cámara real
                containerColor = electricBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 32.dp)
                    .size(60.dp)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Grabar Técnica", modifier = Modifier.size(28.dp))
            }
        }

        // --- Mitad Inferior (Tarjeta Blanca Superpuesta) ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.62f)
                .align(Alignment.BottomCenter),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Cabecera: Nombre y Músculo
                Text(
                    text = exercise.name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Text(
                    text = exercise.targetMuscle,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f), // Detalle en neón sobre blanco
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Ficha Técnica Profesional
                TechnicalInfoRow("Equipamiento", exercise.equipment)
                TechnicalInfoRow("Dificultad", exercise.difficulty)
                TechnicalInfoRow("Series y Reps", "${exercise.sets} series de ${exercise.reps}")
                TechnicalInfoRow("Descanso", exercise.rest)
                TechnicalInfoRow("Calorías", exercise.calories)

                Spacer(modifier = Modifier.height(32.dp))

                // Sección Guía Técnica (Pasos Verticales)
                Text(
                    text = "Guía Técnica Paso a Paso",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Separar la descripción por los números y mostrar en lista vertical
                val steps = exercise.description.split(Regex("\\d+\\.\\s*")).filter { it.isNotBlank() }
                steps.forEachIndexed { index, step ->
                    StepItem(index + 1, step.trim())
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // --- Botón Inferior Dinámico ---
        Button(
            onClick = onToggleFavorite,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFavorite) Color(0xFFFF4444) else Color.Black
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text(
                text = if (isFavorite) "Quitar de Favoritos" else "Añadir a Mis Rutinas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun TechnicalInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.Gray, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        Text(text = value, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}

@Composable
fun StepItem(number: Int, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(Color(0xFFF0F0F0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = Color.DarkGray,
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    }
}

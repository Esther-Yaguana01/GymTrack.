package com.example.gymtrack.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.example.gymtrack.R
import com.example.gymtrack.model.Exercise
import java.util.Locale

@Composable
fun ExerciseDetailScreen(
    exercise: Exercise,
    isFavorite: Boolean,
    weightUnit: String,
    onToggleFavorite: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val electricBlue = Color(0xFF2196F3)

    // Estados para configuración local
    var sets by remember { mutableStateOf(exercise.sets) }
    var reps by remember { mutableStateOf(exercise.reps) }
    var rest by remember { mutableStateOf(exercise.rest) }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            Toast.makeText(context, context.getString(R.string.progress_saved), Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) cameraLauncher.launch()
        else Toast.makeText(context, context.getString(R.string.camera_permission_required), Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.40f)
        ) {
            if (exercise.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = exercise.imageUrl,
                    contentDescription = exercise.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = rememberVectorPainter(Icons.Default.FitnessCenter),
                    error = rememberVectorPainter(Icons.Default.FitnessCenter)
                )
            } else {
                Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                }
            }

            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(16.dp).background(Color.Black.copy(alpha = 0.4f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }

            FloatingActionButton(
                onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                containerColor = electricBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 24.dp, bottom = 16.dp).size(56.dp)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Progreso")
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.65f).align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 24.dp).verticalScroll(rememberScrollState())
            ) {
                Text(text = exercise.name, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = exercise.targetMuscle, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(24.dp))

                // Configuración de Entrenamiento
                Text(text = stringResource(R.string.training_config), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TrainingInput(label = stringResource(R.string.series), value = sets, onValueChange = { sets = it }, Modifier.weight(1f))
                    TrainingInput(label = stringResource(R.string.reps), value = reps, onValueChange = { reps = it }, Modifier.weight(1f))
                    TrainingInput(label = stringResource(R.string.rest), value = rest, onValueChange = { rest = it }, Modifier.weight(1.5f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                val computedWeight = exercise.getDisplayWeight(weightUnit)
                val weightDisplay = if (computedWeight <= 0.0) stringResource(R.string.unit_bodyweight) else String.format(Locale.US, "%.1f %s", computedWeight, weightUnit)
                TechnicalInfoRow(stringResource(R.string.suggested_weight), weightDisplay)
                TechnicalInfoRow(stringResource(R.string.equipment), exercise.equipment)
                TechnicalInfoRow(stringResource(R.string.difficulty), exercise.difficulty)

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = stringResource(R.string.guide_title), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(text = exercise.description, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 22.sp)

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        Button(
            onClick = onToggleFavorite,
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(24.dp).height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isFavorite) Color.Red else MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = if (isFavorite) stringResource(R.string.remove_favorites) else stringResource(R.string.add_favorites), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TrainingInput(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun TechnicalInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

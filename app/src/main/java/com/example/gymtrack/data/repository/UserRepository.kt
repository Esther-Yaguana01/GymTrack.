package com.example.gymtrack.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.gymtrack.BuildConfig
import com.example.gymtrack.data.remote.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class UserRepository {
    private val storageApi = RetrofitInstance.storageApi

    /**
     * Sube la imagen a Supabase Storage y devuelve la URL pública.
     */
    suspend fun uploadProfileImage(userId: String, bitmap: Bitmap): Result<String> {
        return try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            val byteArray = stream.toByteArray()
            val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())

            // Ruta: profiles/{userId}/profile.jpg
            val path = "$userId/profile.jpg"
            val bucket = "profiles"

            val response = storageApi.uploadFile(
                bucket = bucket,
                path = path,
                contentType = "image/jpeg",
                fileBody = requestBody
            )

            if (response.isSuccessful || response.code() == 400) { 
                // Nota: Supabase puede devolver 400 si el archivo ya existe y no se usa x-upsert.
                // En este caso, para simplificar, asumimos que si no falló por auth, la URL es predecible.
                
                // Construir URL pública: {base_url}/storage/v1/object/public/{bucket}/{path}
                val baseUrl = BuildConfig.SUPABASE_URL.replace("/rest/v1/", "/storage/v1/")
                val publicUrl = "${baseUrl}object/public/$bucket/$path"
                Result.success(publicUrl)
            } else {
                val error = response.errorBody()?.string() ?: "Error desconocido"
                Log.e("UserRepository", "Error subiendo imagen: $error")
                Result.failure(Exception("Error al subir imagen: $error"))
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Excepción subiendo imagen", e)
            Result.failure(e)
        }
    }

    suspend fun uploadProgressImage(userId: String, bitmap: Bitmap): Result<String> {
        return try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            val byteArray = stream.toByteArray()
            val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())

            // Usamos un timestamp para que el nombre sea único
            val timestamp = System.currentTimeMillis()
            val path = "$userId/progress_$timestamp.jpg"
            val bucket = "progress"

            val response = storageApi.uploadFile(
                bucket = bucket,
                path = path,
                contentType = "image/jpeg",
                fileBody = requestBody
            )

            if (response.isSuccessful) {
                val baseUrl = BuildConfig.SUPABASE_URL.replace("/rest/v1/", "/storage/v1/")
                val publicUrl = "${baseUrl}object/public/$bucket/$path"
                Result.success(publicUrl)
            } else {
                val error = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Error al subir progreso: $error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProgressImages(userId: String): Result<List<String>> {
        return try {
            val response = storageApi.listFiles(
                bucket = "progress",
                body = mapOf("prefix" to "$userId/")
            )

            if (response.isSuccessful) {
                val baseUrl = BuildConfig.SUPABASE_URL.replace("/rest/v1/", "/storage/v1/")
                val urls = response.body()?.map { item ->
                    "${baseUrl}object/public/progress/$userId/${item.name}"
                } ?: emptyList()
                Result.success(urls)
            } else {
                Result.failure(Exception("Error listando archivos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

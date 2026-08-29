package com.example.gymtrack.data.remote

import android.util.Log
import com.example.gymtrack.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor { chain ->
            // Construir request con headers necesarios para Supabase
            val builder = chain.request().newBuilder()
                .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY)

            try {
                builder.addHeader("Authorization", "Bearer ${BuildConfig.SUPABASE_ANON_KEY}")
            } catch (e: Exception) {
                Log.e("RetrofitInstance", "SUPABASE_ANON_KEY no está configurada correctamente: ${'$'}{e.message}")
            }

            val request = builder.build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .build()

    private fun sanitizeBaseUrl(raw: String): String {
        if (raw.isBlank() || raw == "null") {
            Log.e("RetrofitInstance", "SUPABASE_URL no está configurada en BuildConfig. Verifica local.properties (SUPABASE_URL).")
            throw IllegalStateException("SUPABASE_URL no está configurada. Agrega SUPABASE_URL en local.properties para que Retrofit pueda conectarse a Supabase.")
        }
        return if (raw.endsWith("/")) raw else raw + "/"
    }

    // Base URL para la REST API (tablas)
    val api: ExerciseApi by lazy {
        val base = try { sanitizeBaseUrl(BuildConfig.SUPABASE_URL) } catch (e: Exception) { throw e }
        Retrofit.Builder()
            .baseUrl(base)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExerciseApi::class.java)
    }

    // Base URL para Storage (archivos)
    val storageApi: StorageApi by lazy {
        val storageBaseUrl = try {
            sanitizeBaseUrl(BuildConfig.SUPABASE_URL).replace("/rest/v1/", "/storage/v1/")
        } catch (e: Exception) {
            throw e
        }
        Retrofit.Builder()
            .baseUrl(storageBaseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StorageApi::class.java)
    }
}

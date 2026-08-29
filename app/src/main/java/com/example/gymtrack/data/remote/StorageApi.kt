package com.example.gymtrack.data.remote

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface StorageApi {
    @POST("object/{bucket}/{path}")
    suspend fun uploadFile(
        @Path("bucket") bucket: String,
        @Path("path") path: String,
        @Header("Content-Type") contentType: String,
        @Body fileBody: RequestBody
    ): Response<ResponseBody>

    @POST("object/list/{bucket}")
    suspend fun listFiles(
        @Path("bucket") bucket: String,
        @Body body: Map<String, String> // {"prefix": "userId/"}
    ): Response<List<StorageItemDto>>
}

data class StorageItemDto(
    val name: String,
    val id: String?,
    val updated_at: String?,
    val created_at: String?,
    val last_accessed_at: String?,
    val metadata: Map<String, Any>?
)

package com.example.unikart.data.remote

import com.example.unikart.data.model.CloudinaryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CloudinaryApi {

    @Multipart
    @POST("v1_1/dwacf6tbs/image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody
    ): CloudinaryResponse
}
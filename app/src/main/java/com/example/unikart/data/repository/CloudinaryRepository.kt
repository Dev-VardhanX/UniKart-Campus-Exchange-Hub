package com.example.unikart.data.repository

import android.content.Context
import android.net.Uri
import com.example.unikart.data.remote.CloudinaryApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CloudinaryRepository @Inject constructor(
    private val api: CloudinaryApi
) {

    suspend fun uploadImage(uri: Uri, context: Context): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), bytes!!)
        val body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile)

        val preset = "dshlrsjq"
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val response = api.uploadImage(body, preset)

        return response.secure_url
    }
}
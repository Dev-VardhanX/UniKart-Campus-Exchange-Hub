package com.example.unikart.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storage: FirebaseStorage
) {

    suspend fun uploadImage(uri: Uri): String {
        val fileName = UUID.randomUUID().toString()

        val ref = storage.reference.child("images/$fileName")

        ref.putFile(uri).await()

        return ref.downloadUrl.await().toString()
    }
}
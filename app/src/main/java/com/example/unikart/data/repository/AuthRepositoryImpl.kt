package com.example.unikart.data.repository

import com.example.unikart.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> = try {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun register(
        userName: String,
        email: String,
        password: String
    ): Result<Unit> = try {
        val result = firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .await()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .build()

        result.user?.updateProfile(profileUpdates)?.await()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
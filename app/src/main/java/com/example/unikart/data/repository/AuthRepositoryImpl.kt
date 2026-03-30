package com.example.unikart.data.repository

import com.example.unikart.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth : FirebaseAuth
): AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> = try{
        firebaseAuth.signInWithEmailAndPassword(email,password).await()
        Result.success(Unit)
    } catch (e: Exception){
        Result.failure(e)
    }

    override suspend fun register(email: String, password: String): Result<Unit> = try{
        firebaseAuth.createUserWithEmailAndPassword(email, password)
        Result.success(Unit)
    } catch (e: Exception){
        Result.failure(e)
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
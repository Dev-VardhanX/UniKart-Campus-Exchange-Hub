package com.example.unikart.domain.repository

interface AuthRepository {

    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(userName: String,email: String, password: String): Result<Unit>
    fun logout()
    fun isUserAuthenticated(): Boolean
}
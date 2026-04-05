package com.example.unikart.domain.usecase

import com.example.unikart.domain.repository.AuthRepository


class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(userName: String,email: String, password: String): Result<Unit> {
        return repository.register(userName,email,password)
    }
}

package com.example.unikart.presentation.auth

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unikart.domain.usecase.LoginUseCase
import com.example.unikart.domain.usecase.RegisterUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val googleAuthClient: GoogleAuthUIClient
) : ViewModel() {

    private val _googleUser = MutableStateFlow<FirebaseUser?>(null)
    val googleUser: StateFlow<FirebaseUser?> = _googleUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    var authState by mutableStateOf(AuthState())
        private set



    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = authState.copy(isLoading = true)
            val result = loginUseCase(email, password)
            authState = when {
                result.isSuccess -> authState.copy(isLoading = false, isSuccess = true)
                else -> authState.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            authState = authState.copy(isLoading = true)
            val result = registerUseCase(email, password)
            authState = when {
                result.isSuccess -> authState.copy(isLoading = false, isSuccess = true)
                else -> authState.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }

    // ✅ Correct: delegate to GoogleAuthUIClient
    suspend fun getGoogleSignInIntent() = googleAuthClient.getSignInIntent()

    fun handleGoogleSignInResult(intent: Intent) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = googleAuthClient.signInWithIntent(intent)
                _googleUser.value = user
                authState = authState.copy(isSuccess = true)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut() {
        googleAuthClient.signOut()
        _googleUser.value = null
    }
}

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

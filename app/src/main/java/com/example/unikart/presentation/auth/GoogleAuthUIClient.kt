package com.example.unikart.presentation.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthUIClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("966637562135-dldr68fnd2qom7n8o7j0p07fiu5d5h7v.apps.googleusercontent.com")
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    suspend fun getSignInIntent(): IntentSender? {
        return try {
            val result = oneTapClient.beginSignIn(signInRequest).await()
            result.pendingIntent.intentSender
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun signInWithIntent(intent: Intent): FirebaseUser? {
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleCredential = GoogleAuthProvider.getCredential(credential.googleIdToken, null)
            val result = auth.signInWithCredential(googleCredential).await()
            return result.user
        } catch (e : Exception){
            e.printStackTrace()
            null
        }

    }

    fun signOut() {
        auth.signOut()
    }

    fun getSignedInUser(): FirebaseUser? = auth.currentUser
}
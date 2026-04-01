package com.example.unikart.presentation.Account

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() : ViewModel() {

    val user = FirebaseAuth.getInstance().currentUser

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}
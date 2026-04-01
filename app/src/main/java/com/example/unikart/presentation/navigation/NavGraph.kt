package com.example.unikart.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.unikart.presentation.auth.LoginScreen
import com.example.unikart.presentation.auth.SignupScreen
import com.example.unikart.presentation.main.MainScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    val start = if (FirebaseAuth.getInstance().currentUser != null) {
        "main"
    } else {
        Screen.Login.route
    }
    NavHost(navController = navController, startDestination = start) {

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Signup.route) {
            SignupScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
    }
}

package com.example.unikart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.unikart.presentation.navigation.NavGraph
import com.example.unikart.presentation.navigation.Screen
import com.example.unikart.ui.theme.UniKartTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniKartTheme {

                val navController = rememberNavController()

                val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

                val startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route

                NavGraph(
                    navController = navController, startDestination =startDestination
                )
            }
        }
    }
}

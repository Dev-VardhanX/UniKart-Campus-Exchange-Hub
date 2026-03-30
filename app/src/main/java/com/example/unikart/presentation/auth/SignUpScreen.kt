package com.example.unikart.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unikart.presentation.navigation.Screen

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.authState
    val user by viewModel.googleUser.collectAsState()

    if (state.isSuccess){
        user?.let {
            LaunchedEffect (Unit){
                navController.navigate(Screen.Home.route){
                    popUpTo(Screen.Login.route){inclusive = true}
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF0D0D2B))
                .padding(18.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(18.dp))
//
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .offset(x = (-16).dp, y = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    "Register",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                Row {
                    Text("Already have an account? ", color = Color.Gray)
                    Text("Login",
                        color = Color.Cyan,
                        modifier = Modifier.clickable { navController.navigate(Screen.Login.route) })
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var userName by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }
            var confirmPasswordVisible by remember { mutableStateOf(false) }



            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("User Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person Icon"
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon"
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    var icon =
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    Icon(
                        imageVector = icon,
                        contentDescription = "Password Icon",
                        modifier = Modifier.clickable { passwordVisible= !passwordVisible }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                }
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                singleLine = true,
                label = { Text("Confirm Password") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    var confirmIcon =
                        if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    Icon(
                        imageVector = confirmIcon,
                        contentDescription = null,
                        modifier = Modifier.clickable { confirmPasswordVisible = !confirmPasswordVisible }) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                }
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.register(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0056FF))
            ) {
                Text("Register")
            }

            if (state.isLoading) {
                CircularProgressIndicator()
            }

            state.error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }


        }
    }
}


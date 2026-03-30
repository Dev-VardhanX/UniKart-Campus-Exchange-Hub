package com.example.unikart.presentation.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unikart.R
import com.example.unikart.presentation.navigation.Screen
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
){
    val state = viewModel.authState
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate("main") {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
//            val intent = result.data
            result.data?.let { intent ->
                viewModel.handleGoogleSignInResult(intent)
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
                .height(250.dp)
                .background(Color(0xFF0D0D2B))
                .padding(18.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(18.dp))
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(20.dp)

                            .offset(x = (0).dp, y = 26.dp)
                    )

                    Text(
                        text = "UniKart",
                        color = Color.White,
                        modifier = Modifier.padding(top = 24.dp, start = 6.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )


                }
                Spacer(Modifier.height(30.dp))

                Text(
                    "Sign in to your",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Text("Account", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(16.dp))

                Row {
                    Text("Don't have an account? ", color = Color.Gray)
                    Text(
                        "Sign Up",
                        color = Color.Cyan,
                        modifier = Modifier.clickable { navController.navigate(Screen.Signup.route) })
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
            var passwordVisible by remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
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
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.offset(x = (-12).dp)
                ) {
                    Checkbox(checked = false, onCheckedChange = {})
                    Text("Remember me", fontSize = 14.sp)

                }
                Text("Forgot Password?", color = Color(0xFF0056FF), fontSize = 14.sp)
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.login(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0056FF))
            ) {
                Text("Log In")
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }



            Spacer(Modifier.height(28.dp))

            Text(
                text = "Or Continue with",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(28.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly
            ) {

                val coroutineScope = rememberCoroutineScope()


                Button(
                    onClick = {
                        coroutineScope.launch {
                            val intentSender = viewModel.getGoogleSignInIntent()
                            intentSender?.let {
                                val intentSenderRequest = IntentSenderRequest.Builder(it).build()
                                launcher.launch(intentSenderRequest)   // ✅ Correct
                            }
                        }

                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)

                ) {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo_lighttheme_circle),
                            contentDescription = "Google Logo",
                            modifier = Modifier
                                .size(40.dp)

                        )
                        Text(
                            "Sign in with Google",
                            color = Color.Black,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
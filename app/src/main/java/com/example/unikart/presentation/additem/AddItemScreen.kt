package com.example.unikart.presentation.additem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unikart.data.model.Item
import com.google.firebase.auth.FirebaseAuth
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun AddItemScreen(
    navController: NavHostController,
    viewModel: AddItemViewModel = hiltViewModel()
) {

    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val selectedTypes = remember { mutableStateListOf<String>() }

    val isLoading = viewModel.isLoading
    val isSuccess = viewModel.isSuccess

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text("Add Item", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") }
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") }
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") }
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Type")

        Row {
            Checkbox(
                checked = selectedTypes.contains("Sell"),
                onCheckedChange = {
                    if (it) selectedTypes.add("Sell")
                    else selectedTypes.remove("Sell")
                }
            )
            Text("Sell")
        }

        Row {
            Checkbox(
                checked = selectedTypes.contains("Rent"),
                onCheckedChange = {
                    if (it) selectedTypes.add("Rent")
                    else selectedTypes.remove("Rent")
                }
            )
            Text("Rent")
        }

        Row {
            Checkbox(
                checked = selectedTypes.contains("Exchange"),
                onCheckedChange = {
                    if (it) selectedTypes.add("Exchange")
                    else selectedTypes.remove("Exchange")
                }
            )
            Text("Exchange")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text("Pick Image")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && price.isNotBlank()) {
                    val currentUser = FirebaseAuth.getInstance().currentUser

                    val item = currentUser?.let {
                        Item(
                            title = title,
                            price = price,
                            category = category,
                            types = selectedTypes.toList(),
                            description = description,
                            imageUrl = "",
                            location = location,
                            userId = it.uid,
                            userName = it.displayName ?: "Unknown",
                            userEmail = it.email ?: ""
                        )
                    }

                    item?.let {
                        viewModel.addItem(it, imageUri)
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        {
            Text("Add Item")
        }
        if (isLoading) {
            CircularProgressIndicator()
        }
        LaunchedEffect(isSuccess) {
            if (isSuccess) {
                title = ""
                price = ""
                category = ""
                description = ""
                location = ""
                selectedTypes.clear()
                navController.popBackStack()
            }
        }


    }
}
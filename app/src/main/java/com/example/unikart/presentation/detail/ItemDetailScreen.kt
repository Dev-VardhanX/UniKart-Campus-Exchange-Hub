package com.example.unikart.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun ItemDetailsScreen(
    navController: NavHostController,
    itemId: String,
    viewModel: ItemDetailsViewModel = hiltViewModel()
) {

    val item = viewModel.item
    val isLoading = viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.loadItem(itemId)
    }

    if (isLoading) {
        Text("Loading...")
    } else {
        item?.let {

            Column(modifier = Modifier.padding(16.dp)) {

                Text(it.title, style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(8.dp))

                Text("₹${it.price}")

                Text("Location: ${it.location}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Category: ${it.category}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Type: ${it.types.joinToString()}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Description:")
                Text(it.description)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Seller:")
                Text(it.userName)
                Text(it.userEmail)
            }
        }
    }
}
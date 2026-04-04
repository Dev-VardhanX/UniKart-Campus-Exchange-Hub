package com.example.unikart.presentation.Account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unikart.presentation.components.ItemCard
import com.example.unikart.presentation.navigation.Screen
import com.google.firebase.auth.FirebaseAuth


@Composable
fun MyListingsScreen(
    navController: NavHostController,
    viewModel: MyListingsViewModel = hiltViewModel()
) {

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val items by viewModel.items.collectAsState()

    var selectedTab by remember { mutableStateOf("All") }

    LaunchedEffect(Unit) {
        userId?.let { viewModel.loadItems(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "My Listings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            listOf("All", "Available", "Sold").forEach { tab ->

                Button(
                    onClick = { selectedTab = tab }
                ) {
                    Text(tab)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val filteredItems = when(selectedTab){
            "Available" -> items.filter { !it.isSold }
            "Sold" -> items.filter { it.isSold }
            else -> items
        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredItems) { item ->
                ItemCard(
                    item = item,
                    isFavorite = false,
                    onFavoriteClick = {},
                    onClick = {
                        navController.navigate(
                            Screen.ItemDetails.createRoute(item.id)
                        )
                    }
                )
            }
        }
    }
}
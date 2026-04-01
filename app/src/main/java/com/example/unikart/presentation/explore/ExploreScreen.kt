package com.example.unikart.presentation.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unikart.presentation.components.ItemCard
import com.example.unikart.presentation.home.HomeViewModel
import com.example.unikart.presentation.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ExploreScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val items by viewModel.items.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.onSearchChange(it) },
            label = { Text("Search items...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            items(items) { item ->
                ItemCard(
                    item = item,
                    isFavorite = viewModel.favoriteIds.contains(item.id),
                    onFavoriteClick = {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@ItemCard
                        viewModel.toggleFavorite(userId, item.id)
                    },
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
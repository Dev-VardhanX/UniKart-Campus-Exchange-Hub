package com.example.unikart.presentation.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unikart.presentation.components.ItemCard
import com.example.unikart.presentation.home.HomeViewModel
import com.example.unikart.presentation.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.LaunchedEffect

@Composable
fun FavouritesScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val items by viewModel.items.collectAsState()
    val favoriteItems = viewModel.favoriteItems

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        userId?.let {
            viewModel.loadFavorites(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Your Favorites",
            style = MaterialTheme.typography.headlineMedium
        )

        if (favoriteItems.isEmpty()) {
            Text(
                text = "No favorites yet 😢",
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(favoriteItems) { item ->

                    ItemCard(
                        item = item,
                        isFavorite = true,
                        onFavoriteClick = {
                            userId?.let {
                                viewModel.toggleFavorite(it, item.id)
                            }
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
}
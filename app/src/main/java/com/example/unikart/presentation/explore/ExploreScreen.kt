package com.example.unikart.presentation.explore

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unikart.presentation.components.ItemCard
import com.example.unikart.presentation.home.HomeViewModel
import com.example.unikart.presentation.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

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


        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            listOf("Sell", "Rent", "Exchange").forEach { type ->

                FilterChip(
                    selected = viewModel.selectedType == type,
                    onClick = {
                        viewModel.onTypeChange(
                            if (viewModel.selectedType == type) "" else type
                        )
                    },
                    label = { Text(type) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        var priceRange by remember { mutableStateOf(0f..10000f) }

        Text("Price Range: ₹${priceRange.start.toInt()} - ₹${priceRange.endInclusive.toInt()}")

        RangeSlider(
            value = priceRange,
            onValueChange = {
                priceRange = it
            },
            onValueChangeFinished = {
                viewModel.onPriceChange(
                    priceRange.start,
                    priceRange.endInclusive
                )
            },
            valueRange = 0f..10000f
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onTypeChange("")
                viewModel.onCategoryChange("")
                viewModel.onSearchChange("")
            }
        ) {
            Text("Clear Filters")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            listOf("Books", "Electronics", "Clothes").forEach { category ->

                FilterChip(
                    selected = viewModel.selectedCategory == category,
                    onClick = {
                        viewModel.onCategoryChange(
                            if (viewModel.selectedCategory == category) "" else category
                        )
                    },
                    label = { Text(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2)
//        ) {
//            items(items) { item ->
//                ItemCard(
//                    item = item,
//                    isFavorite = viewModel.favoriteIds.contains(item.id),
//                    onFavoriteClick = {
//                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@ItemCard
//                        viewModel.toggleFavorite(userId, item.id)
//                    },
//                    onClick = {
//                        navController.navigate(
//                            Screen.ItemDetails.createRoute(item.id)
//                        )
//                    }
//                )
//            }
//        }
        if (items.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "No items found 😕",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Try adjusting filters",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(items) { item ->
                    ItemCard(
                        item = item,
                        isFavorite = viewModel.favoriteIds.contains(item.id),
                        onFavoriteClick = {
                            val userId = FirebaseAuth.getInstance().currentUser?.uid
                                ?: return@ItemCard

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
}
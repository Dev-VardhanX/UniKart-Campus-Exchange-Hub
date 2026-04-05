package com.example.unikart.presentation.explore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unikart.R
import com.example.unikart.presentation.common.itemCategories
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
    val listState = rememberLazyListState()

    val showFilters = listState.firstVisibleItemIndex == 0 &&
            listState.firstVisibleItemScrollOffset < 10

    var priceRange by remember { mutableStateOf(0f..10000f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 0.dp)
    ) {

        Text(
            text = "Explore",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.onSearchChange(it) },
            placeholder = { Text("Search items...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AnimatedVisibility(
                    visible = showFilters,
                    enter = fadeIn() + slideInVertically { -it / 2 },
                    exit = fadeOut() + slideOutVertically { -it / 2 }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Type",
                            style = MaterialTheme.typography.labelLarge
                        )

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

                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = viewModel.selectedCategory.isBlank(),
                                onClick = { viewModel.onCategoryChange("") },
                                label = { Text("All") }
                            )

                            itemCategories.forEach { itemCategory ->
                                FilterChip(
                                    selected = viewModel.selectedCategory == itemCategory,
                                    onClick = { viewModel.onCategoryChange(itemCategory) },
                                    label = { Text(itemCategory) }
                                )
                            }
                        }

                        Text(
                            text = "Price Range",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Text(
                            text = "₹${priceRange.start.toInt()} - ₹${priceRange.endInclusive.toInt()}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        RangeSlider(
                            value = priceRange,
                            onValueChange = { priceRange = it },
                            onValueChangeFinished = {
                                viewModel.onPriceChange(
                                    priceRange.start,
                                    priceRange.endInclusive
                                )
                            },
                            valueRange = 0f..10000f
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Clear",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        viewModel.onTypeChange("")
                                        viewModel.onCategoryChange("")
                                        viewModel.onSearchChange("")
                                        priceRange = 0f..10000f
                                        viewModel.onPriceChange(0f, 10000f)
                                    }
                            )
                        }
                    }
                }
            }
            if (items.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No items found 😕")
                    }
                }
            } else {
                items(items.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { item ->
                            Box(modifier = Modifier.weight(1f)) {
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

                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
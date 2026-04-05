package com.example.unikart.presentation.favourites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unikart.R
import com.example.unikart.presentation.components.ItemCard
import com.example.unikart.presentation.home.HomeViewModel
import com.example.unikart.presentation.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

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

    if (favoriteItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "UniKart Logo",
                        modifier = Modifier.size(34.dp)
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = "Favourites",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "No favourites yet",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Items you save will appear here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 90.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "UniKart Logo",
                        modifier = Modifier.size(34.dp)
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = "Favourites",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Your saved items",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

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

//package com.example.unikart.presentation.favourites
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.GridItemSpan
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import com.example.unikart.presentation.components.ItemCard
//import com.example.unikart.presentation.home.HomeViewModel
//import com.example.unikart.presentation.navigation.Screen
//import com.google.firebase.auth.FirebaseAuth
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.res.painterResource
//import com.example.unikart.R
//
//@Composable
//fun FavouritesScreen(
//    navController: NavHostController,
//    viewModel: HomeViewModel = hiltViewModel()
//) {
//
//    val items by viewModel.items.collectAsState()
//    val favoriteItems = viewModel.favoriteItems
//
//    val userId = FirebaseAuth.getInstance().currentUser?.uid
//
//    LaunchedEffect(Unit) {
//        userId?.let {
//            viewModel.loadFavorites(it)
//        }
//    }
//
////    Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .padding(16.dp)
////    ) {
////
////        Text(
////            text = "Your Favorites",
////            style = MaterialTheme.typography.headlineMedium
////        )
//
//
//
//        if (favoriteItems.isEmpty()) {
//            Text(
//                text = "No favorites yet 😢",
//                modifier = Modifier.padding(top = 16.dp)
//            )
//        }
//
//        else {
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                contentPadding = androidx.compose.foundation.layout.PaddingValues(
//                    start = 16.dp,
//                    end = 16.dp,
//                    top = 12.dp,
//                    bottom = 12.dp
//                ),
//                verticalArrangement = Arrangement.spacedBy(12.dp),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            )
//            {
//                item(span = { GridItemSpan(maxLineSpan) }) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.logo),
//                            contentDescription = "UniKart Logo",
//                            modifier = Modifier.size(34.dp)
//                        )
//
//                        Spacer(modifier = Modifier.size(8.dp))
//
//                        Text(
//                            text = "UniKart",
//                            style = MaterialTheme.typography.headlineMedium
//                        )
//                    }
//                }
//
//                item(span = { GridItemSpan(maxLineSpan) }) {
//                    Spacer(modifier = Modifier.height(2.dp))
//                }
//
//                item(span = { GridItemSpan(maxLineSpan) }) {
//                    Text(
//                        text = "Favorites",
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                }
//
//                item(span = { GridItemSpan(maxLineSpan) }) {
//                    Spacer(modifier = Modifier.height(2.dp))
//                }
//
//                items(favoriteItems) { item ->
//
//                    ItemCard(
//                        item = item,
//                        isFavorite = true,
//                        onFavoriteClick = {
//                            userId?.let {
//                                viewModel.toggleFavorite(it, item.id)
//                            }
//                        },
//                        onClick = {
//                            navController.navigate(
//                                Screen.ItemDetails.createRoute(item.id)
//                            )
//                        }
//                    )
//                }
//            }
//    }
//}
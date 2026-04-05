package com.example.unikart.presentation.main

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.unikart.presentation.Account.AccountScreen
import com.example.unikart.presentation.Account.MyListingsScreen
import com.example.unikart.presentation.additem.AddItemScreen
import com.example.unikart.presentation.components.BottomNavBar
import com.example.unikart.presentation.detail.ItemDetailsScreen
import com.example.unikart.presentation.explore.ExploreScreen
import com.example.unikart.presentation.favourites.FavouritesScreen
import com.example.unikart.presentation.home.HomeScreen
import com.example.unikart.presentation.navigation.Screen

@Composable
fun MainScreen(
    rootNController: NavHostController
) {

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        },

        floatingActionButton = {
            if (currentRoute == Screen.Home.route) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.AddItem.route)
                    },
                    modifier = Modifier
                        .offset(y = 40.dp),

                    shape = RoundedCornerShape(100.dp),
                    containerColor = Color(0xFFC19DE7),
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Item")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(Screen.Explore.route) {
                ExploreScreen(navController)
            }
            composable(Screen.Favourites.route) {
                FavouritesScreen(navController)
            }
            composable(Screen.Account.route) {
                AccountScreen(
                    navController = navController,
                    rootNavController = rootNController
                )
            }
            composable(Screen.AddItem.route) {
                AddItemScreen(navController)
            }
            composable(
                route = Screen.ItemDetails.route
            ) { backStackEntry ->

                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""

                ItemDetailsScreen(
                    navController = navController,
                    itemId = itemId
                )
            }
            composable(Screen.MyListings.route) {
                MyListingsScreen(navController)
            }
            composable(
                route = Screen.EditItem.route
            ) { backStackEntry ->

                val itemId = backStackEntry.arguments?.getString("itemId")

                AddItemScreen(
                    navController = navController,
                    itemId = itemId
                )
            }
        }
    }
}

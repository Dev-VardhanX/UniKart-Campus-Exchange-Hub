package com.example.unikart.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Favourites : Screen("favourites")
    object Account : Screen("account")
    object AddItem : Screen("additem")
    object ItemDetails : Screen("itemdetails/{itemId}") {
        fun createRoute(itemId: String) = "itemdetails/$itemId"
    }
}
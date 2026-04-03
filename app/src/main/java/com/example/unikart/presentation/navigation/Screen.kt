package com.example.unikart.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Favourites : Screen("favourites")
    object Account : Screen("account")
    object AddItem : Screen("additem")
    object EditItem : Screen("edititem/{itemId}") {
        fun createRoute(itemId: String) = "edititem/$itemId"
    }
    object ItemDetails : Screen("itemdetails/{itemId}") {
        fun createRoute(itemId: String) = "itemdetails/$itemId"
    }
    object MyListings : Screen("my_listings")

}
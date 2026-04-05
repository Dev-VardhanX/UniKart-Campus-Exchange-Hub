package com.example.unikart.data.model

data class Item(
    val id: String = "",
    val title: String = "",
    val price: String = "",
    val category: String = "",
    val types: List<String> = emptyList(),
    val description: String = "",
    val imageUrls: List<String> = emptyList(),
    val location: String = "",
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userPhone: String = "",
    val sold: Boolean = false,
    val rentPrice: String = "",
    val rentDuration: String = "",
    val exchangeFor: String = ""
)
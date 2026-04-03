package com.example.unikart.domain.repository

import com.example.unikart.data.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    suspend fun addItem(item: Item): Result <Unit>


    fun getItems(): Flow<List<Item>>

    suspend fun getItemById(id: String): Item?

    suspend fun addToFavorites(userId: String, itemId: String)

    suspend fun removeFromFavorites(userId: String, itemId: String)

    fun getFavorites(userId: String): Flow<List<String>>

    fun getItemsByUser(userId: String): Flow<List<Item>>

    suspend fun updateItem(item: Item)

    suspend fun deleteItem(itemId: String)
}
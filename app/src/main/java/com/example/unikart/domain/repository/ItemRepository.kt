package com.example.unikart.domain.repository

import com.example.unikart.data.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    suspend fun addItem(item: Item): Result <Unit>


    fun getItems(): Flow<List<Item>>

    suspend fun getItemById(id: String): Item?
}
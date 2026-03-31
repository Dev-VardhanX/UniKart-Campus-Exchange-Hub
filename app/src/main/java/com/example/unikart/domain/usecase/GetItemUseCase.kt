package com.example.unikart.domain.usecase

import com.example.unikart.data.model.Item
import com.example.unikart.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    operator fun invoke(): Flow<List<Item>> {
        return repository.getItems()
    }
}
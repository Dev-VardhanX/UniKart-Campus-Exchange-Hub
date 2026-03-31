package com.example.unikart.domain.usecase

import com.example.unikart.data.model.Item
import com.example.unikart.domain.repository.ItemRepository
import javax.inject.Inject

class AddItemUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(item: Item): Result<Unit> {
        return repository.addItem(item)
    }
}
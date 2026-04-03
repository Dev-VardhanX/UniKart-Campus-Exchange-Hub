package com.example.unikart.domain.usecase

import com.example.unikart.domain.repository.ItemRepository
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(itemId: String) {
        repository.deleteItem(itemId)
    }
}
package com.example.unikart.domain.usecase

import com.example.unikart.data.model.Item
import com.example.unikart.domain.repository.ItemRepository
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(id: String): Item? {
        return repository.getItemById(id)
    }
}
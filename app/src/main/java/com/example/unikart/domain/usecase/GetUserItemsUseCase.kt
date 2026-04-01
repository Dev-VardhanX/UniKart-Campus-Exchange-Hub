package com.example.unikart.domain.usecase

import com.example.unikart.domain.repository.ItemRepository
import javax.inject.Inject

class GetUserItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    operator fun invoke(userId: String) = repository.getItemsByUser(userId)
}
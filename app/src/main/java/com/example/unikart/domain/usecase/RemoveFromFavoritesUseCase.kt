package com.example.unikart.domain.usecase

import com.example.unikart.domain.repository.ItemRepository
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(userId: String, itemId: String) {
        repository.removeFromFavorites(userId, itemId)
    }
}
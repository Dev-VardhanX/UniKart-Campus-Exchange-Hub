package com.example.unikart.domain.usecase

import com.example.unikart.domain.repository.ItemRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    operator fun invoke(userId: String) =
        repository.getFavorites(userId)
}
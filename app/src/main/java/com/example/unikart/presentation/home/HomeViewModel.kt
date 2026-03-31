package com.example.unikart.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unikart.data.model.Item
import com.example.unikart.domain.usecase.AddToFavoritesUseCase
import com.example.unikart.domain.usecase.GetFavoritesUseCase
import com.example.unikart.domain.usecase.GetItemsUseCase
import com.example.unikart.domain.usecase.RemoveFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase
) : ViewModel() {

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch {
            getItemsUseCase().collect { itemList ->
                _items.value = itemList
            }
        }
    }

    var favoriteIds by mutableStateOf<List<String>>(emptyList())
        private set

    fun loadFavorites(userId: String) {
        viewModelScope.launch {
            getFavoritesUseCase(userId).collect {
                favoriteIds = it
            }
        }
    }

    fun toggleFavorite(userId: String, itemId: String) {
        viewModelScope.launch {
            if (favoriteIds.contains(itemId)) {
                removeFromFavoritesUseCase(userId, itemId)
            } else {
                addToFavoritesUseCase(userId, itemId)
            }
        }
    }
}
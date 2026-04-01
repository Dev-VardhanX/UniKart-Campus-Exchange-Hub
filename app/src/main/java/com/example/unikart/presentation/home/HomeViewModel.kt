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

    var searchQuery by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf("")
        private set

    var selectedType by mutableStateOf("")
        private set

    var minPrice by mutableStateOf(0f)
        private set

    var maxPrice by mutableStateOf(10000f)
        private set

    init {
        fetchItems()
    }

    fun onPriceChange(min: Float, max: Float) {
        minPrice = min
        maxPrice = max
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch {
            getItemsUseCase().collect { itemList ->

                val filtered = itemList.filter { item ->

                    val matchesSearch = item.title.contains(searchQuery, true)

                    val matchesCategory =
                        selectedCategory.isEmpty() || item.category == selectedCategory

                    val matchesType =
                        selectedType.isEmpty() || item.types.contains(selectedType)

                    val price = item.price.toFloatOrNull() ?: 0f
                    val matchesPrice = price in minPrice..maxPrice

                    matchesSearch && matchesCategory && matchesType && matchesPrice
                }

                _items.value = filtered
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

    val favoriteItems: List<Item>
        get() = _items.value.filter { item ->
            favoriteIds.contains(item.id)
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

//    fun onSearchChange(query: String) {
//        searchQuery = query
//    }
//
//    fun onCategoryChange(category: String) {
//        selectedCategory = category
//    }
//
//    fun onTypeChange(type: String) {
//        selectedType = type
//    }

    fun onSearchChange(query: String) {
        searchQuery = query
        fetchItems()
    }

    fun onCategoryChange(category: String) {
        selectedCategory = category
        fetchItems()
    }

    fun onTypeChange(type: String) {
        selectedType = type
        fetchItems()
    }
}



package com.example.unikart.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unikart.data.model.Item
import com.example.unikart.domain.usecase.DeleteItemUseCase
import com.example.unikart.domain.usecase.GetItemByIdUseCase
import com.example.unikart.domain.usecase.UpdateItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val updateItemUseCase: UpdateItemUseCase
) : ViewModel() {

    var item by mutableStateOf<Item?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val result = getItemByIdUseCase(itemId)
                item = result
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            deleteItemUseCase(itemId)
        }
    }

    fun markAsSold(currentItem: Item) {
        viewModelScope.launch {
            try {
                updateItemUseCase(currentItem.copy(sold = true))

                item = currentItem.copy(sold = true)


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleSold(currentItem: Item) {
        viewModelScope.launch {
            try {
                val newStatus = !currentItem.sold

                updateItemUseCase(
                    currentItem.copy(sold = newStatus)
                )

                // update UI instantly
                item = currentItem.copy(sold = newStatus)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
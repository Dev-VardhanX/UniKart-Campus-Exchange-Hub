package com.example.unikart.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unikart.data.model.Item
import com.example.unikart.domain.usecase.DeleteItemUseCase
import com.example.unikart.domain.usecase.GetItemByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
) : ViewModel() {

    var item by mutableStateOf<Item?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadItem(id: String) {
        viewModelScope.launch {
            isLoading = true
            item = getItemByIdUseCase(id)
            isLoading = false
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            deleteItemUseCase(itemId)
        }
    }
}
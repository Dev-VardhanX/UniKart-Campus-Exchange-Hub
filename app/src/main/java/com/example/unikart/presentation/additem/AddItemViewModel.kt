package com.example.unikart.presentation.additem

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unikart.data.model.Item
import com.example.unikart.data.repository.StorageRepository
import com.example.unikart.domain.usecase.AddItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val addItemUseCase: AddItemUseCase,
   // private val storageRepository: StorageRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var isSuccess by mutableStateOf(false)
        private set

    fun addItem(item: Item) {
        viewModelScope.launch {
            isLoading = true
            try {

//                val imageUrl = imageUri?.let {
//                    storageRepository.uploadImage(it)
//                } ?: ""
//
//                val updatedItem = item.copy(imageUrl = imageUrl)

                addItemUseCase(item)

                isSuccess = true

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}
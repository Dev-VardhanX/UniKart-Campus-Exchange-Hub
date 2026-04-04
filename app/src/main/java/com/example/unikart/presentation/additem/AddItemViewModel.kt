package com.example.unikart.presentation.additem

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unikart.data.model.Item
import com.example.unikart.data.repository.CloudinaryRepository
import com.example.unikart.domain.usecase.AddItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import com.example.unikart.domain.usecase.GetItemUseCase
import com.example.unikart.domain.usecase.UpdateItemUseCase


@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val addItemUseCase: AddItemUseCase,
    private val cloudinaryRepository: CloudinaryRepository,
    private val getItemUseCase: GetItemUseCase,
    private val updateItemUseCase: UpdateItemUseCase
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var isSuccess by mutableStateOf(false)
        private set

    var existingItem by mutableStateOf<Item?>(null)
        private set

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            try {
                val item = getItemUseCase(itemId)
                existingItem = item
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            isLoading = true
            try {
                updateItemUseCase(item)
                isSuccess = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun addItem(item: Item, imageUris: List<Uri>, context: Context) {
        viewModelScope.launch {
            isLoading = true
            try {

                val imageUrls = if (imageUris.isNotEmpty()) {
                    cloudinaryRepository.uploadImages(imageUris, context)
                } else {
                    emptyList()
                }

                val updatedItem = item.copy(imageUrls = imageUrls)

                addItemUseCase(updatedItem)

                isSuccess = true

            } catch (e: Exception) {
                e.printStackTrace()
                println("UPLOAD ERROR: ${e.message}")
                Log.e("CLOUDINARY_ERROR", e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }
}
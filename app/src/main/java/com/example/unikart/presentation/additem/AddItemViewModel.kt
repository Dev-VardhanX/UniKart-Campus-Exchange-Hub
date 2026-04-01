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


@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val addItemUseCase: AddItemUseCase,
    private val cloudinaryRepository: CloudinaryRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var isSuccess by mutableStateOf(false)
        private set

    fun addItem(item: Item, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            isLoading = true
            try {

                val imageUrl = imageUri?.let {
                    cloudinaryRepository.uploadImage(it, context)
                } ?: ""

                val updatedItem = item.copy(imageUrl = imageUrl)

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
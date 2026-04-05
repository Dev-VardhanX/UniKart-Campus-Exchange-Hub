package com.example.unikart.presentation.additem

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.unikart.data.model.Item
import com.example.unikart.presentation.common.itemCategories
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    navController: NavHostController,
    viewModel: AddItemViewModel = hiltViewModel(),
    itemId: String? = null
) {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var rentPrice by remember { mutableStateOf("") }
    var rentDuration by remember { mutableStateOf("") }
    var exchangeFor by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val imageUris = remember { mutableStateListOf<Uri>() }
    val selectedTypes = remember { mutableStateListOf<String>() }

    val isLoading = viewModel.isLoading
    val isSuccess = viewModel.isSuccess
    val context = LocalContext.current
    val existingItem = viewModel.existingItem

    LaunchedEffect(itemId) {
        if (itemId != null) {
            viewModel.loadItem(itemId)
        }
    }

    LaunchedEffect(existingItem) {
        existingItem?.let {
            title = it.title
            price = it.price
            category = it.category
            description = it.description
            location = it.location
            phone = it.userPhone
            rentPrice = it.rentPrice
            rentDuration = it.rentDuration
            exchangeFor = it.exchangeFor

            selectedTypes.clear()
            selectedTypes.addAll(it.types)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris.clear()
        imageUris.addAll(uris)
    }

    val isSell = selectedTypes.contains("Sell")
    val isRent = selectedTypes.contains("Rent")
    val isExchange = selectedTypes.contains("Exchange")

    val isValid = when {
        isSell -> title.isNotBlank() && price.isNotBlank() && category.isNotBlank()
        isRent -> title.isNotBlank() && rentPrice.isNotBlank() && rentDuration.isNotBlank() && category.isNotBlank()
        isExchange -> title.isNotBlank() && exchangeFor.isNotBlank() && category.isNotBlank()
        else -> false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = if (existingItem != null) "Edit Item" else "Add Item",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "List something for sell, rent or exchange",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Basic Details",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    itemCategories.forEach { itemCategory ->
                        DropdownMenuItem(
                            text = { Text(itemCategory) },
                            onClick = {
                                category = itemCategory
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number (WhatsApp)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Listing Type",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Sell", "Rent", "Exchange").forEach { type ->
                    FilterChip(
                        selected = selectedTypes.contains(type),
                        onClick = {
                            if (selectedTypes.contains(type)) {
                                selectedTypes.remove(type)
                            } else {
                                selectedTypes.add(type)
                            }
                        },
                        label = { Text(type) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isSell || isRent || isExchange) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Type Details",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isSell) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (isRent) {
                    OutlinedTextField(
                        value = rentPrice,
                        onValueChange = { rentPrice = it },
                        label = { Text("Rent Price") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = rentDuration,
                        onValueChange = { rentDuration = it },
                        label = { Text("Duration (day/week/month)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (isExchange) {
                    OutlinedTextField(
                        value = exchangeFor,
                        onValueChange = { exchangeFor = it },
                        label = { Text("Looking to exchange with...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Images",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Pick Images")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                imageUris.forEach { uri ->
                    Box(
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (isValid) {
                    val currentUser = FirebaseAuth.getInstance().currentUser

                    val item = currentUser?.let {
                        Item(
                            id = existingItem?.id ?: "",
                            title = title,
                            price = price,
                            category = category,
                            types = selectedTypes.toList(),
                            description = description,
                            imageUrls = emptyList(),
                            location = location,
                            userId = it.uid,
                            userName = it.displayName ?: "Unknown",
                            userEmail = it.email ?: "",
                            userPhone = phone,
                            rentPrice = rentPrice,
                            rentDuration = rentDuration,
                            exchangeFor = exchangeFor
                        )
                    }

                    item?.let {
                        if (existingItem != null) {
                            viewModel.updateItem(it)
                        } else {
                            viewModel.addItem(item, imageUris, context)
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(16.dp)
        ) {if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(if (existingItem != null) "Update Item" else "Add Item")
        }

        }



        LaunchedEffect(isSuccess) {
            if (isSuccess) {
                title = ""
                price = ""
                category = ""
                description = ""
                location = ""
                phone = ""
                rentPrice = ""
                rentDuration = ""
                exchangeFor = ""
                selectedTypes.clear()
                navController.popBackStack()
            }
        }
    }
}
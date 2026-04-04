package com.example.unikart.presentation.additem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unikart.data.model.Item
import com.google.firebase.auth.FirebaseAuth
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage

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
    val imageUris = remember { mutableStateListOf<Uri>() }

    val selectedTypes = remember { mutableStateListOf<String>() }

    val isLoading = viewModel.isLoading
    val isSuccess = viewModel.isSuccess

    val context = LocalContext.current

    LaunchedEffect(itemId) {
        if (itemId != null) {
            viewModel.loadItem(itemId)
        }
    }

    val existingItem = viewModel.existingItem

    LaunchedEffect(existingItem) {
        existingItem?.let {
            title = it.title
            price = it.price
            category = it.category
            description = it.description
            location = it.location

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text("Add Item", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )

        if (selectedTypes.contains("Sell")){
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") }
            )
        }

        if (selectedTypes.contains("Rent")) {

            OutlinedTextField(
                value = rentPrice,
                onValueChange = { rentPrice = it },
                label = { Text("Rent Price") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = rentDuration,
                onValueChange = { rentDuration = it },
                label = { Text("Duration (day/week/month)") },
                modifier = Modifier.fillMaxWidth()
            )
        }


        if (selectedTypes.contains("Exchange")) {

            OutlinedTextField(
                value = exchangeFor,
                onValueChange = { exchangeFor = it },
                label = { Text("Looking to exchange with...") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") }
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") }
        )
        var phone by remember { mutableStateOf("") }

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number (WhatsApp)") }
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Type")

        Row {
            Checkbox(
                checked = selectedTypes.contains("Sell"),
                onCheckedChange = {
                    if (it) selectedTypes.add("Sell")
                    else selectedTypes.remove("Sell")
                }
            )
            Text("Sell")
        }

        Row {
            Checkbox(
                checked = selectedTypes.contains("Rent"),
                onCheckedChange = {
                    if (it) selectedTypes.add("Rent")
                    else selectedTypes.remove("Rent")
                }
            )
            Text("Rent")
        }

        Row {
            Checkbox(
                checked = selectedTypes.contains("Exchange"),
                onCheckedChange = {
                    if (it) selectedTypes.add("Exchange")
                    else selectedTypes.remove("Exchange")
                }
            )
            Text("Exchange")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text("Pick Images")
        }

        Spacer(modifier = Modifier.height(20.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            imageUris.forEach { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        val isSell = selectedTypes.contains("Sell")
        val isRent = selectedTypes.contains("Rent")
        val isExchange = selectedTypes.contains("Exchange")

        val isValid = when {
            isSell -> title.isNotBlank() && price.isNotBlank()
            isRent -> title.isNotBlank() && rentPrice.isNotBlank() && rentDuration.isNotBlank()
            isExchange -> title.isNotBlank() && exchangeFor.isNotBlank()
            else -> false
        }

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
                            viewModel.addItem(item, imageUris , context)
                        }
                    }
                }
            }
        )
        {
            Text("Add Item")
        }
        if (isLoading) {
            CircularProgressIndicator()
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
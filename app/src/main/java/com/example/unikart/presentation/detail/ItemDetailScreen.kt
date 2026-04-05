package com.example.unikart.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.unikart.presentation.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ItemDetailsScreen(
    navController: NavHostController,
    itemId: String,
    viewModel: ItemDetailsViewModel = hiltViewModel()
) {
    val item = viewModel.item
    val isLoading = viewModel.isLoading
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    LaunchedEffect(itemId) {
        viewModel.loadItem(itemId)
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    item?.let { currentItem ->

        val isOwner = currentUser?.uid == currentItem.userId

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            if (currentItem.imageUrls.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currentItem.imageUrls) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = currentItem.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 12.dp, bottom = 4.dp)
                                .width(320.dp)
                                .height(240.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No image available",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                if (currentItem.sold) {
                    Text(
                        text = "SOLD",
                        color = MaterialTheme.colorScheme.onError,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.error)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = currentItem.title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (currentItem.types.contains("Sell") && currentItem.price.isNotBlank()) {
                    Text(
                        text = "Price: ₹${currentItem.price}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                }

                if (currentItem.types.contains("Rent") && currentItem.rentPrice.isNotBlank()) {
                    Text(
                        text = "Rent: ₹${currentItem.rentPrice} / ${currentItem.rentDuration}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                }

                if (
                    currentItem.types.contains("Exchange") &&
                    !currentItem.types.contains("Sell") &&
                    !currentItem.types.contains("Rent")
                ) {
                    Text(
                        text = "Available for Exchange",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                }

                Text(
                    text = "📍 ${currentItem.location}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = {},
                        label = { Text(currentItem.category) }
                    )

                    currentItem.types.forEach { type ->
                        AssistChip(
                            onClick = {},
                            label = { Text(type) }
                        )
                    }
                }

                if (currentItem.types.contains("Exchange") && currentItem.exchangeFor.isNotBlank()) {
                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Exchange For",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentItem.exchangeFor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Divider()
                Spacer(modifier = Modifier.height(20.dp))

                if (isOwner) {
                    Text(
                        text = "Manage Listing",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                navController.navigate(
                                    Screen.EditItem.createRoute(currentItem.id)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Edit")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.deleteItem(currentItem.id)
                                navController.popBackStack()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Delete")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.toggleSold(currentItem)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            if (currentItem.sold) "Mark as Available" else "Mark as Sold"
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentItem.description.ifBlank { "No description provided." },
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(20.dp))
                Divider()
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Seller Information",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Text(
                            text = currentItem.userName.ifBlank { "Unknown Seller" },
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = currentItem.userEmail,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (currentItem.userPhone.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentItem.userPhone,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val phone = currentItem.userPhone.trim()

                        if (phone.isNotBlank()) {
                            val message =
                                "Hi ${currentItem.userName}, I'm interested in your item \"${currentItem.title}\". Is it still available?"

                            val url = "https://wa.me/$phone?text=${Uri.encode(message)}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    },
                    enabled = !currentItem.sold && currentItem.userPhone.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        when {
                            currentItem.sold -> "Item Sold"
                            currentItem.userPhone.isBlank() -> "Phone Not Available"
                            else -> "Chat on WhatsApp"
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
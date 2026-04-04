package com.example.unikart.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    LaunchedEffect(Unit) {
        viewModel.loadItem(itemId)
    }

    val currentUser = FirebaseAuth.getInstance().currentUser



    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        item?.let {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                if (item != null) {
                    if (currentUser?.uid == item.userId) {
                        Button(onClick = {
                            navController.navigate(
                                Screen.EditItem.createRoute(item.id)
                            )
                        }) { Text("Edit") }

                        Button(
                            onClick = {
                                viewModel.deleteItem(item.id)
                                navController.popBackStack()
                            }
                        ) {
                            Text("Delete")
                        }

                        Button(
                            onClick = {
                                viewModel.toggleSold(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (it.isSold) "Mark as Available" else "Mark as Sold")
                        }
                    }
                }

                
                LazyRow {
                    items(it.imageUrls) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier
                                .width(300.dp)
                                .height(250.dp)
                                .padding(8.dp)
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "₹${it.price}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "📍 ${it.location}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(
                            onClick = {},
                            label = { Text(it.category) }
                        )

                        AssistChip(
                            onClick = {},
                            label = { Text(it.types.joinToString()) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider()

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Divider()

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Seller Information",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(it.userName)
                            Text(it.userEmail)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    val context = LocalContext.current

                    Button(
                        onClick = {


                            val phone = it.userPhone
                            val message = "Hi ${it.userName}, I'm interested in your item \"${it.title}\" listed for ₹${it.price}. Is it still available?"

                            val url = "https://wa.me/$phone?text=${Uri.encode(message)}"

                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)

                            context.startActivity(intent)
                        },
                        enabled = !it.isSold,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (it.isSold) "Item Sold" else "Chat on WhatsApp")
                    }
                }
            }
        }
    }
}
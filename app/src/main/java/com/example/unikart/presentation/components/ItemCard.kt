package com.example.unikart.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.unikart.data.model.Item
import coil.compose.AsyncImage

@Composable
fun ItemCard(
    item: Item,
    onClick:() -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            AsyncImage(
                model = item.imageUrls.firstOrNull(),
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            if (item.isSold) {
                Text(
                    text = "SOLD",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )


                Icon(
                    imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Favorite",
                    modifier = Modifier.clickable { onFavoriteClick() }
                )
            }

            Row {
                item.types.forEach { type ->
                    AssistChip(
                        onClick = {},
                        label = { Text(type) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = when(type) {
                                "Sell" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                "Rent" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                                "Exchange" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                                else -> MaterialTheme.colorScheme.surface
                            }
                        ),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }

            val priceText = when {
                item.types.contains("Sell") -> "₹${item.price}"
                item.types.contains("Rent") -> "₹${item.rentPrice} / ${item.rentDuration}"
                item.types.contains("Exchange") -> "For ${item.exchangeFor}"
                else -> ""
            }

            Text(
                text = "${priceText}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = item.location,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
package com.example.unikart.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.unikart.data.model.Item

@Composable
fun ItemCard(
    item: Item,
    onClick:() -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "₹${item.price}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = item.location,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
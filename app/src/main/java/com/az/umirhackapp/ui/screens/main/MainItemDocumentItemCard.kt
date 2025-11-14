package com.az.umirhackapp.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.server.Product

@Deprecated("Использую отдельный экран для этого, но пусть будет")
@Composable
fun MainItemDocumentItemCard(
    item: DocumentItem,
    onClick: (DocumentItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick(item) },
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                content = {
                    val product = item.product ?: Product(-1, "Неизвестно", "Неизвестно", "Неизвестно", -1)
                    Text(
                        text = "Название: " + product.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Описание: " + product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Кол-во по документам: " + item.quantityExpected,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Кол-во на складе: " + item.quantityActual,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if ((item.quantityExpected - item.quantityActual).toInt() != 0)
                        Text(
                            "Обнаружено расхождение на " + (item.quantityExpected - item.quantityActual).toString() + " единиц(ы)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                }
            )
        }
    )
}
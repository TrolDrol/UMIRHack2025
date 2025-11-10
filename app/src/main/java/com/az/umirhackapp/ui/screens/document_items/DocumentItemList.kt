package com.az.umirhackapp.ui.screens.document_items

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem

// Список карт товаров
@SuppressLint("UnrememberedMutableState")
@Composable
fun DocumentItemsList(
    documentItems: MutableState<List<DocumentItem>>,
    selectedDocument: Document
) {
    println("documentItems: ${documentItems.value}")
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(documentItems.value.size) { ind ->
            println("1. DocumentItemsList: $ind")
            println("2. DocumentItemsList: ${documentItems.value}")
            println("3. DocumentItemsList: ${documentItems.value[ind]}")
            val itemState = mutableStateOf(documentItems.value[ind])
            println("4. DocumentItemsList: ${itemState.value}")
            DocumentItemCard(
                documentItem = itemState,
                isEditable = selectedDocument.status == "in_progress",
                onEditeDocumentItem = { documentItem ->
                    val new = documentItems.value.toMutableList()
                    new[ind] = documentItem
                    documentItems.value = new
                }
            )
        }

        if (documentItems.value.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Нет товаров в документе",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Карточка товара
@Composable
fun DocumentItemCard(
    documentItem: MutableState<DocumentItem>,
    isEditable: Boolean,
    onEditeDocumentItem: (DocumentItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (documentItem.value.quantityExpected == documentItem.value.quantityActual) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.errorContainer
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Информация о товаре
            documentItem.value.product?.let { product ->
                Text(
                    product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!product.barcode.isNullOrEmpty()) {
                    Text(
                        "Штрих-код: ${product.barcode}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } ?: Text(
                "Товар ID: ${documentItem.value.productId}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Количества
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Ожидается: ${documentItem.value.quantityExpected}")
                    Text(
                        "Фактически: ${documentItem.value.quantityActual}",
                        color = if (documentItem.value.quantityExpected == documentItem.value.quantityActual) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                }

                if (isEditable) {
                    QuantityEditor(
                        currentQuantity = documentItem.value.quantityActual,
                        onEditeQuantity = { newValue ->
                            documentItem.value = documentItem.value.copy(quantityActual = newValue)
                            onEditeDocumentItem(documentItem.value)
                        }
                    )
                }
            }

            // Предупреждение о расхождении
            if (documentItem.value.quantityExpected != documentItem.value.quantityActual) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Расхождение: ${documentItem.value.quantityActual - documentItem.value.quantityExpected}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// Редактор количества
@Composable
fun QuantityEditor(
    currentQuantity: Double,
    onEditeQuantity: (Double) -> Unit
) {
    TextButton(
        onClick = { onEditeQuantity(currentQuantity - 1) },
        enabled = currentQuantity > 0,
        content = {
            Text(
                "Уменьшить кол-во",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )

    // Прошлая версия
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        IconButton(
//            onClick = { onEditeQuantity(currentQuantity - 1) },
//            enabled = currentQuantity > 0
//        ) {
//            Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
//        }
//
//        Text(
//            text = currentQuantity.toInt().toString(),
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(horizontal = 8.dp)
//        )
//
//        IconButton(
//            onClick = { onEditeQuantity(currentQuantity + 1) }
//        ) {
//            Icon(Icons.Default.Add, contentDescription = "Увеличить")
//        }
//    }
}
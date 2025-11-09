package com.az.umirhackapp.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.test.TestViewModel
import com.az.umirhackapp.test.documents

@Composable
fun DocumentItemsScreen(
    viewModel: TestViewModel = viewModel(),
    selectedDocument: Document,
    documentItems: List<DocumentItem>,
    onBackClick: () -> Unit,
    onQRScannerClick: () -> Unit,
    onUpdateQuantity: (DocumentItem, Double) -> Unit,
    onCompleteDocument: () -> Unit,
    onCancelDocument: () -> Unit
) {
    var showCompletionDialog by remember { mutableStateOf(false) }
    val scannedProduct by viewModel.scannedProduct.collectAsState()

    LaunchedEffect(scannedProduct) {
        if (scannedProduct != null)
            onUpdateQuantity(
                documentItems.find { it.product == scannedProduct }!!,
                documentItems.find { it.product == scannedProduct }!!.quantityActual + 1.0
            )
    }

    Scaffold(
        topBar = {
            DocumentItemsTopBar(
                selectedDocument = selectedDocument,
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            if (selectedDocument.status == "in_progress") {
                ScanFloatingActionButton(onQRScannerClick)
            }
        },
        bottomBar = {
            DocumentActionsBottomBar(
                selectedDocument = selectedDocument,
                documentItems = documentItems,
                onCompleteClick = { showCompletionDialog = true },
                onCancelClick = onCancelDocument
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Статус документа и статистика
            DocumentHeader(selectedDocument, documentItems)

            // Список товаров
            DocumentItemsList(
                documentItems = documentItems,
                selectedDocument = selectedDocument,
                onUpdateQuantity = onUpdateQuantity
            )
        }

        // Диалог завершения документа
        if (showCompletionDialog) {
            CompleteDocumentDialog(
                selectedDocument = selectedDocument,
                documentItems = documentItems,
                onConfirm = {
                    onCompleteDocument()
                    showCompletionDialog = false
                },
                onDismiss = { showCompletionDialog = false }
            )
        }
    }
}

// TopBar с информацией о документе
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentItemsTopBar(
    selectedDocument: Document,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    "Документ ${selectedDocument.number}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    getStatusText(selectedDocument.status),
                    style = MaterialTheme.typography.bodySmall,
                    color = getStatusColor(selectedDocument.status)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }
        }
    )
}

// Плавающая кнопка сканирования
@Composable
fun ScanFloatingActionButton(onQRScannerClick: () -> Unit) {
    FloatingActionButton(
        onClick = onQRScannerClick,
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            Icons.Default.QrCodeScanner,
            contentDescription = "Сканировать штрих-код"
        )
    }
}

// Header с информацией о документе
@Composable
fun DocumentHeader(selectedDocument: Document, documentItems: List<DocumentItem>) {
    val stats = calculateDocumentStats(documentItems)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Основная информация
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Склад: ${selectedDocument.warehouse?.name ?: "Не указан"}")
                    Text("Дата: ${selectedDocument.documentDate}")
                }
                Text(
                    getStatusText(selectedDocument.status),
                    color = getStatusColor(selectedDocument.status),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Статистика
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Всего", stats.totalItems.toString())
                StatItem("Совпало", stats.matchedCount.toString())
                StatItem("Расхождение", stats.discrepancyCount.toString())
            }

            // Прогресс сканирования
            if (selectedDocument.status == "in_progress") {
                Spacer(modifier = Modifier.height(8.dp))
                ScanningProgress(stats)
            }
        }
    }
}

// Список товаров в документе
@Composable
fun DocumentItemsList(
    documentItems: List<DocumentItem>,
    selectedDocument: Document,
    onUpdateQuantity: (DocumentItem, Double) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(documentItems.size) { item ->
            DocumentItemCard(
                documentItem = documentItems[item],
                isEditable = selectedDocument.status == "in_progress",
                onUpdateQuantity = { newQuantity ->
                    onUpdateQuantity(documentItems[item], newQuantity)
                }
            )
        }

        if (documentItems.isEmpty()) {
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
    documentItem: DocumentItem,
    isEditable: Boolean,
    onUpdateQuantity: (Double) -> Unit
) {
    var currentQuantity by remember { mutableStateOf(documentItem.quantityActual) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (documentItem.quantityExpected == documentItem.quantityActual) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.errorContainer
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Информация о товаре
            documentItem.product?.let { product ->
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
                "Товар ID: ${documentItem.productId}",
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
                    Text("Ожидается: ${documentItem.quantityExpected}")
                    Text(
                        "Фактически: ${documentItem.quantityActual}",
                        color = if (documentItem.quantityExpected == documentItem.quantityActual) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                }

                if (isEditable) {
                    QuantityEditor(
                        currentQuantity = currentQuantity,
                        onQuantityChange = { newQuantity ->
                            currentQuantity = newQuantity
                            onUpdateQuantity(newQuantity)
                        }
                    )
                }
            }

            // Предупреждение о расхождении
            if (documentItem.quantityExpected != documentItem.quantityActual) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Расхождение: ${documentItem.quantityActual - documentItem.quantityExpected}",
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
    onQuantityChange: (Double) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onQuantityChange(currentQuantity - 1) },
            enabled = currentQuantity > 0
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Уменьшить")
        }

        Text(
            text = currentQuantity.toInt().toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        IconButton(
            onClick = { onQuantityChange(currentQuantity + 1) }
        ) {
            Icon(Icons.Default.Add, contentDescription = "Увеличить")
        }
    }
}

// BottomBar с действиями
@Composable
fun DocumentActionsBottomBar(
    selectedDocument: Document,
    documentItems: List<DocumentItem>,
    onCompleteClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val stats = calculateDocumentStats(documentItems)

    Surface(
        tonalElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            when (selectedDocument.status) {
                "draft" -> {
                    Button(
                        onClick = { /* Начать инвентаризацию */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Начать инвентаризацию")
                    }
                }
                "in_progress" -> {
                    Button(
                        onClick = onCompleteClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (stats.discrepancyCount > 0) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                    ) {
                        Text(
                            if (stats.discrepancyCount > 0) {
                                "Завершить с расхождениями"
                            } else {
                                "Завершить инвентаризацию"
                            }
                        )
                    }
                }
                "completed" -> {
                    Text(
                        "Инвентаризация завершена",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (selectedDocument.status != "completed" && selectedDocument.status != "cancelled") {
                TextButton(
                    onClick = onCancelClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отменить документ")
                }
            }
        }
    }
}

// Диалог завершения документа
@Composable
fun CompleteDocumentDialog(
    selectedDocument: Document,
    documentItems: List<DocumentItem>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val stats = calculateDocumentStats(documentItems)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Завершение инвентаризации") },
        text = {
            Column {
                Text("Вы уверены, что хотите завершить инвентаризацию?")

                if (stats.discrepancyCount > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Обнаружены расхождения в ${stats.discrepancyCount} товарах",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Всего товаров: ${stats.totalItems}")
                Text("Совпало: ${stats.matchedCount}")
                Text("Расхождения: ${stats.discrepancyCount}")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Завершить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

// Вспомогательные функции
private fun calculateDocumentStats(items: List<DocumentItem>): DocumentStats {
    val totalItems = items.size
    val matchedCount = items.count { it.quantityExpected == it.quantityActual }
    val discrepancyCount = totalItems - matchedCount

    return DocumentStats(totalItems, matchedCount, discrepancyCount)
}

private data class DocumentStats(
    val totalItems: Int,
    val matchedCount: Int,
    val discrepancyCount: Int
)

private fun getStatusText(status: String): String {
    return when (status) {
        "draft" -> "Черновик"
        "in_progress" -> "В процессе"
        "completed" -> "Завершен"
        "cancelled" -> "Отменен"
        else -> status
    }
}

@Composable
private fun getStatusColor(status: String): Color {
    return when (status) {
        "draft" -> MaterialTheme.colorScheme.onSurfaceVariant
        "in_progress" -> MaterialTheme.colorScheme.primary
        "completed" -> MaterialTheme.colorScheme.primary
        "cancelled" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ScanningProgress(stats: DocumentStats) {
    val progress = if (stats.totalItems > 0) {
        stats.matchedCount.toFloat() / stats.totalItems
    } else {
        0f
    }

    Column {
        Text(
            "Прогресс сканирования: ${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDocumentItemsScreen() {
    DocumentItemsScreen(

        selectedDocument = documents[1],
        documentItems = documents[1].items,
        onBackClick = {  },
        onQRScannerClick = {  },
        onCancelDocument = {  },
        onUpdateQuantity = {documentItem, double ->
            println("$documentItem $double")
        },
        onCompleteDocument = {  }
    )
}
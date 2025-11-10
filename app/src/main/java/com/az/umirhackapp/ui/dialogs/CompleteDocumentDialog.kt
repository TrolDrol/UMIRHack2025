package com.az.umirhackapp.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.ui.screens.document_items.calculateDocumentStats

// Диалог завершения документа
@Composable
fun CompleteDocumentDialog(
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
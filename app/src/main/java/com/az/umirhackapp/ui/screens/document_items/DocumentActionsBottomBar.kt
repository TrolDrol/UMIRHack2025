package com.az.umirhackapp.ui.screens.document_items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem

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
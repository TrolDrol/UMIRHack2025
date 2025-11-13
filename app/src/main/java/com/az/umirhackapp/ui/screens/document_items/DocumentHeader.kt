package com.az.umirhackapp.ui.screens.document_items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem

// Header с информацией о документе
@Composable
fun DocumentHeader(
    selectedDocument: Document,
    documentItems: MutableState<List<DocumentItem>>
) {
    val stats = calculateDocumentStats(documentItems.value)

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
                    Text("Дата: ${selectedDocument.documentDate.substring(0..19)}")
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
//            if (selectedDocument.status == "in_progress") {
//                Spacer(modifier = Modifier.height(8.dp))
//                ScanningProgress(stats)
//            }
        }
    }
}
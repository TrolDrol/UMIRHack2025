package com.az.umirhackapp.ui.screens.document_items

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.az.umirhackapp.server.DocumentItem

// Вспомогательные функции
fun calculateDocumentStats(items: List<DocumentItem>): DocumentStats {
    val totalItems = items.size
    val matchedCount = items.count { it.quantityExpected == it.quantityActual }
    val discrepancyCount = totalItems - matchedCount

    return DocumentStats(totalItems, matchedCount, discrepancyCount)
}

data class DocumentStats(
    val totalItems: Int,
    val matchedCount: Int,
    val discrepancyCount: Int
)

fun getStatusText(status: String): String {
    return when (status) {
        "draft" -> "Черновик"
        "in_progress" -> "В процессе"
        "completed" -> "Завершен"
        "cancelled" -> "Отменен"
        else -> status
    }
}

@Composable
fun getStatusColor(status: String): Color {
    return when (status) {
        "draft" -> MaterialTheme.colorScheme.onSurfaceVariant
        "in_progress" -> MaterialTheme.colorScheme.primary
        "completed" -> MaterialTheme.colorScheme.primary
        "cancelled" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
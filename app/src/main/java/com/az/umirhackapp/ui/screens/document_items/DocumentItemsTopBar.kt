package com.az.umirhackapp.ui.screens.document_items

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.az.umirhackapp.server.Document

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
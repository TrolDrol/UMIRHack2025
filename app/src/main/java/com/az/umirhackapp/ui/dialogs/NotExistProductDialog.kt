package com.az.umirhackapp.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NotExistProductDialog(
    barcode: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Такого товара нет в базе данных!") },
        text = { Text("Товар со штрих-кодом $barcode не найден в базе данных") },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Хорошо")
            }
        }
    )
}
package com.az.umirhackapp.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun OnBackClickDocumentItemsScreenDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выйти из документа?") },
        text = {
            Text("При выходе из документа все действия, которые вы с ним произвели сохранятся\n" +
                    "Вы уверены что хотите выйти?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Да, выйти из документа")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Нет")
            }
        }
    )
}
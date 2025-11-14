package com.az.umirhackapp.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.Document

@Composable
fun SendReportDialog(
    document: Document,
    onDismiss: () -> Unit,
    onConfirm: (String, Boolean, Boolean) -> Unit
) {
    var recipientEmail by remember { mutableStateOf("") }
    var includePDF by remember { mutableStateOf(false) }
    var includeCSV by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Отправка отчета") },
        text = {
            Column {
                Text("Отчет по документу №${document.number}")

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = recipientEmail,
                    onValueChange = { recipientEmail = it },
                    label = { Text("Email получателя") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = includePDF,
                        onCheckedChange = { includePDF = it }
                    )
                    Text("Включить PDF отчет")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = includeCSV,
                        onCheckedChange = { includeCSV = it }
                    )
                    Text("Включить CSV отчет")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(recipientEmail, includePDF, includeCSV)
                },
                enabled = recipientEmail.isNotBlank()
            ) {
                Text("Отправить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
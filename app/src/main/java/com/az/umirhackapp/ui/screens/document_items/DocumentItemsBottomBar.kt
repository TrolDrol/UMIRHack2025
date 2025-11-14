package com.az.umirhackapp.ui.screens.document_items

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.test.documents
import com.az.umirhackapp.ui.theme.AppTheme

// BottomBar с действиями
@Composable
fun DocumentActionsBottomBar(
    selectedDocument: Document,
    documentItems: List<DocumentItem>,
    visibilityScaffold: MutableFloatState,
    onStartInventory: (Document) -> Unit,
    onCompleteClick: () -> Unit,
    onSendReport: () -> Unit
) {
    val stats = calculateDocumentStats(documentItems)

    Surface(
        tonalElevation = 8.dp
    ) {
        Column {
            when (selectedDocument.status) {
                "draft" -> {
                    Button(
                        onClick = { onStartInventory(selectedDocument) },
                        content = {
                            Text(
                                "Начать инвентаризацию",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.background
                            )
                                  },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                "in_progress" -> {
                    Column {
                        Text("Видимость камеры:")
                        Slider(
                            value = visibilityScaffold.floatValue,
                            onValueChange = { visibilityScaffold.floatValue = it },
                            valueRange = 0.1f..1f
                        )
                        Button(
                            onClick = onCompleteClick,
                            modifier = Modifier.fillMaxWidth(),
                            content = {
                                Text(
                                    if (stats.discrepancyCount > 0) {
                                        "Завершить с расхождениями"
                                    } else {
                                        "Завершить инвентаризацию"
                                    },
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.background
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (stats.discrepancyCount > 0) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.primary
                                }
                            )
                        )
                    }
                }

                "completed" -> {
                    Text(
                        "Инвентаризация завершена",
                        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }

            Button(
                onClick = onSendReport,
                content = {
                    Text(
                        text = "Отправить отчёт по почте",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.background
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview()
@Composable
fun PreviewDocumentActionsBottomBar() {
    AppTheme {
        DocumentActionsBottomBar(
            documents[0],
            documents[0].items,
            mutableFloatStateOf(1f),
            {},
            {},
            {}
        )
    }
}
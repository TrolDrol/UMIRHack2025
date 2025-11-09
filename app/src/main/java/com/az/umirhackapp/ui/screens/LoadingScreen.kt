package com.az.umirhackapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Альтернативная версия с кастомными параметрами
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    message: String = "Загрузка...",
    subMessage: String = "",
    progressIndicatorSize: Dp = 64.dp,
    strokeWidth: Dp = 4.dp,
    content: (() -> Unit) = { }
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(progressIndicatorSize),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = strokeWidth
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        if (!subMessage.isEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        // Логика загрузки
         LaunchedEffect(Unit) {
             content()
         }
    }
}
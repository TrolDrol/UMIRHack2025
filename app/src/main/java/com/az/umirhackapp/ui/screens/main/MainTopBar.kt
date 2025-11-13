package com.az.umirhackapp.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.User

@Composable
fun MainTopBar(
    user: User,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            content = {
                Text(
                    text = "Добро пожаловать!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }
        )

        // Иконка уведомлений
        IconButton(
            onClick = onNotificationClick,
            content = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Уведомления"
                )
            }
        )

        // Иконка профиля
        IconButton(
            onClick = onProfileClick,
            content = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Профиль"
                )
            }
        )
    }
}
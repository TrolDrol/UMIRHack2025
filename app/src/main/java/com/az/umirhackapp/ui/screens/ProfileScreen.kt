package com.az.umirhackapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.az.umirhackapp.server.NetworkModule
import com.az.umirhackapp.server.User
import com.az.umirhackapp.server.auth.AuthRepository
import com.az.umirhackapp.server.auth.AuthViewModel
import com.az.umirhackapp.server.auth.TokenService
import com.az.umirhackapp.ui.Screen
import com.az.umirhackapp.ui.theme.AppTheme

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onSettingsClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit,
    systemInDarkTheme: Boolean = true
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    content = {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back Click",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )

                Text(
                    text = Screen.PROFILE.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(
                    onClick = onSettingsClick,
                    content = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Настройки",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Background(systemInDarkTheme)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Используем реальные данные пользователя
            val user = currentUser ?: User(
                id = 0,
                email = "Загрузка...",
                fullName = "Загрузка...",
                createdAt = "01.01.1970",
                organization = emptyList()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Имя пользователя
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Карточка с информацией о профиле
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                content = {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        ProfileInfoItem(
                            title = "Дата регистрации",
                            value = user.createdAt.toString()
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка редактирования профиля
            Button(
                onClick = onEditProfileClick,
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "Редактировать профиль",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка выхода
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                content = {
                    Text(
                        text = "Выйти",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}

@Composable
fun ProfileInfoItem(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProfileScreen() {
    val authRepository = AuthRepository(NetworkModule.apiService)
    val tokenService = TokenService(LocalContext.current)
    val authViewModel: AuthViewModel = viewModel { AuthViewModel(authRepository, tokenService) }

    AppTheme {
        ProfileScreen(
            authViewModel = authViewModel,
            onSettingsClick = {
            },
            onEditProfileClick = {
                // В будущем можно добавить экран редактирования профиля
            },
            onLogoutClick = {
                authViewModel.logout()
            },
            onBackClick = {
            }
        )
    }
}
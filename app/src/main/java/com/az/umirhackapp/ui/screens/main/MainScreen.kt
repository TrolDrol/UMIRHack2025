package com.az.umirhackapp.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.User
import com.az.umirhackapp.test.TestViewModel
import com.az.umirhackapp.ui.Screen

@Composable
fun MainScreen(
    user: User = User(0, "example@com", "Иванов Иван", null, null),
    viewModel: TestViewModel,
    label: String = "",
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onQRScannerClick: () -> Unit,
    onBackClick: () -> Unit,
    loadContent: () -> Unit,
    content: @Composable ((String) -> Unit)
) {
    LaunchedEffect(Unit) {
        loadContent()
    }
    var searchText by remember { mutableStateOf("") }
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState.error != null) {
            snackBarHostState.showSnackbar("Error: ${uiState.error}")
        }
        if (uiState.successMessage != null) {
            snackBarHostState.showSnackbar("SuccessMessage: ${uiState.successMessage}")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
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
        },
        floatingActionButton = {
            ScanFloatingActionButton(onQRScannerClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Поисковая строка
            SearchBar(
                searchText,
                { searchText = it }
            )

            Row {
                if (label != Screen.MAIN_ORGANIZATIONS.title)
                    IconButton(
                        onClick = { onBackClick() },
                        content = {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Назад",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                // Заголовок раздела
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Список элементов
            content(searchText)
        }
    }
}

// Плавающая кнопка сканирования
@Composable
fun ScanFloatingActionButton(onQRScannerClick: () -> Unit) {
    FloatingActionButton(
        onClick = onQRScannerClick,
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            Icons.Default.QrCodeScanner,
            contentDescription = "Сканировать штрих-код"
        )
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun PreviewMainScreen() {
//    val tokenService = TokenService(LocalContext.current)
//    val inventoryRepository = InventoryRepository(NetworkModule.apiService, tokenService)
//    val inventoryViewModel = viewModel { InventoryViewModel(inventoryRepository) }
//
//    AppTheme {
//        MainScreen(
//            User(0, "example@com", "Иванов Иван", null, null),
//            inventoryViewModel,
//            label = Screen.MAIN_ORGANIZATIONS.title,
//            onProfileClick = {
//            },
//            onNotificationClick = {
//                // В будущем можно добавить экран уведомлений
//            },
//            onQRScannerClick = {
//            },
//            onBackClick = { },
//            loadContent = {
//                inventoryViewModel.loadOrganizations()
//            },
//            content = { searchText ->
//                LazyColumnItems(
//                    organizations,
//                    { org ->
//                        inventoryViewModel.selectOrganization(org)
//                    },
//                    Screen.MAIN_ORGANIZATIONS,
//                    searchText
//                )
//            }
//        )
//    }
//}
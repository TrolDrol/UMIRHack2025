package com.az.umirhackapp.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.User
import com.az.umirhackapp.server.inventory.InventoryViewModel
import com.az.umirhackapp.ui.Screen
import com.az.umirhackapp.ui.screens.Background

@Composable
fun MainScreen(
    user: User = User(0, "example@com", "Иванов Иван", null, null),
    viewModel: InventoryViewModel,
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
            MainTopBar(
                user,
                onNotificationClick,
                onProfileClick
            )
        },
        floatingActionButton = {
            ScanFloatingActionButton(onQRScannerClick)
        }
    ) { paddingValues ->
        Background()
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
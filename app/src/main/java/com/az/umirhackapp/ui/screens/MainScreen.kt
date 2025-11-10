package com.az.umirhackapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.server.Organization
import com.az.umirhackapp.server.Product
import com.az.umirhackapp.server.User
import com.az.umirhackapp.server.Warehouse
import com.az.umirhackapp.test.documents
import com.az.umirhackapp.ui.Screen

@Composable
fun MainScreen(
    user: User = User(0, "example@com", "Иванов Иван", "+71231231212"),
    label: String = "",
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onQRScannerClick: () -> Unit,
    onBackClick: () -> Unit,
    loadContent: () -> Unit,
    content: @Composable () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        loadContent()
    }

    val snackBarHostState = remember { SnackbarHostState() }

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
            content()
        }
    }
}

@Composable
fun <T> LazyColumnItems(
    items: List<T>,
    onClick: (T) -> Unit,
    route: Screen
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items.size) { item ->
            when(route) {
                Screen.REGISTRATION -> TODO()
                Screen.LOGIN -> TODO()
                Screen.LOADING -> TODO()
                Screen.PROFILE -> TODO()
                Screen.SETTINGS -> TODO()
                Screen.MAIN -> TODO()
                Screen.MAIN_ORGANIZATIONS -> MainItemOrganizationCard(items[item] as Organization,
                    onClick as (Organization) -> Unit
                )
                Screen.MAIN_WAREHOUSES -> MainItemWarehouseCard(items[item] as Warehouse,
                    onClick as (Warehouse) -> Unit
                )
                Screen.MAIN_DOCUMENTS ->  MainItemDocumentsCard(items[item] as Document,
                    onClick as (Document) -> Unit
                )
                Screen.MAIN_DOCUMENT_ITEMS -> MainItemDocumentItemCard(items[item] as DocumentItem,
                    onClick as (DocumentItem) -> Unit)
                Screen.QR_SCANNER -> TODO()
                Screen.PERMISSION_REQUEST -> TODO()
            }
        }
    }
}

@Composable
fun MainItemOrganizationCard(
    item: Organization,
    onClick: (Organization) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick(item) },
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.address!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.phone!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    )
}

@Composable
fun MainItemWarehouseCard(
    item: Warehouse,
    onClick: (Warehouse) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick(item) },
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.address!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    )
}

@Composable
fun MainItemDocumentsCard(
    item: Document,
    onClick: (Document) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick(item) },
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = "Type: " + item.type,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Status: " + item.status,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Date: " + item.documentDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Created At: " + item.createdById,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    )
}

@Composable
fun MainItemDocumentItemCard(
    item: DocumentItem,
    onClick: (DocumentItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick(item) },
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                content = {
                    val product = item.product ?: Product(-1, "Неизвестно", "Неизвестно", "Неизвестно", -1)
                    Text(
                        text = "Название: " + product.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Описание: " + product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Кол-во по документам: " + item.quantityExpected,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Кол-во на складе: " + item.quantityActual,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if ((item.quantityExpected - item.quantityActual).toInt() != 0)
                        Text(
                            "Обнаружено расхождение на " + (item.quantityExpected - item.quantityActual).toString() + " единиц(ы)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                }
            )
        }
    )
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Поиск..."
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                        innerTextField()
                    }

                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchTextChange("") },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Очистить",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
            }
        )
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

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    LazyColumnItems(
        items = documents[0].items,
        {},
        Screen.MAIN_DOCUMENT_ITEMS
    )
}
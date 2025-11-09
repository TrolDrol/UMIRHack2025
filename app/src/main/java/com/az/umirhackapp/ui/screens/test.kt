package com.az.umirhackapp.ui.screens
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.ExposedDropdownMenuBox
//import androidx.compose.material3.ExposedDropdownMenuDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.az.umirhackapp.server.Document
//import com.az.umirhackapp.server.Product
//import com.az.umirhackapp.server.inventory.InventoryUiState
//import com.az.umirhackapp.server.inventory.InventoryViewModel
//
//@Composable
//fun InventoryScreen(
//    viewModel: InventoryViewModel = viewModel(),
//    onQRScanner: () -> Unit,
//    onLoading: @Composable () -> Unit
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    val documents by viewModel.documents.collectAsState()
//    val scannedProduct by viewModel.scannedProduct.collectAsState()
//
//    LaunchedEffect(Unit) {
//        println("1")
//        viewModel.loadOrganizations()
//        println("2")
//    }
//
//    val snackBarHostState = remember { SnackbarHostState() }
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        snackbarHost = { SnackbarHost(snackBarHostState) },
//        content = { paddingValues ->
//            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
//                // Выбор организации и склада
//                OrganizationSelector(uiState, viewModel)
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Кнопка сканирования
//                Button(
//                    onClick = onQRScanner,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Сканировать штрих-код")
//                }
//
//                // Отсканированный товар
//                scannedProduct?.let { product ->
//                    Spacer(modifier = Modifier.height(16.dp))
//                    ScannedProductCard(product, viewModel)
//                }
//
//                // Список документов
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    "Документы инвентаризации",
//                    style = MaterialTheme.typography.headlineSmall
//                )
//                DocumentsList(documents, viewModel)
//            }
//
//            // Обработка состояний
//            HandleUiState(uiState, viewModel, onLoading, snackBarHostState)
//        }
//    )
//
//
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun OrganizationSelector(uiState: InventoryUiState, viewModel: InventoryViewModel) {
//    var organizationExpanded by remember { mutableStateOf(false) }
//    var warehouseExpanded by remember { mutableStateOf(false) }
//
//    Column {
//        // Выбор организации
//        ExposedDropdownMenuBox(
//            expanded = organizationExpanded,
//            onExpandedChange = { organizationExpanded = !organizationExpanded }
//        ) {
//            TextField(
//                value = uiState.selectedOrganization?.name ?: "Выберите организацию",
//                onValueChange = {},
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = organizationExpanded) },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            ExposedDropdownMenu(
//                expanded = organizationExpanded,
//                onDismissRequest = { organizationExpanded = false }
//            ) {
//                uiState.organizations.forEach { organization ->
//                    DropdownMenuItem(
//                        text = { Text(organization.name) },
//                        onClick = {
//                            viewModel.selectOrganization(organization)
//                            organizationExpanded = false
//                        }
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Выбор склада
//        ExposedDropdownMenuBox(
//            expanded = warehouseExpanded,
//            onExpandedChange = { warehouseExpanded = !warehouseExpanded }
//        ) {
//            TextField(
//                value = uiState.selectedWarehouse?.name ?: "Выберите склад",
//                onValueChange = {},
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = warehouseExpanded) },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            ExposedDropdownMenu(
//                expanded = warehouseExpanded,
//                onDismissRequest = { warehouseExpanded = false }
//            ) {
//                uiState.warehouses.forEach { warehouse ->
//                    DropdownMenuItem(
//                        text = { Text(warehouse.name) },
//                        onClick = {
//                            viewModel.selectWarehouse(warehouse)
//                            warehouseExpanded = false
//                            viewModel.loadDocuments(uiState.selectedOrganization?.id ?: return@DropdownMenuItem, warehouse.id)
//                        }
//                    )
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ScannedProductCard(product: Product, viewModel: InventoryViewModel) {
//    var selectedDocument by remember { mutableStateOf<Document?>(null) }
//    val documents by viewModel.documents.collectAsState()
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.1f))
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text("Найден товар:", style = MaterialTheme.typography.titleMedium)
//            Text(product.name, style = MaterialTheme.typography.bodyLarge)
//            Text("Штрих-код: ${product.barcode}", style = MaterialTheme.typography.bodyMedium)
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Выбор документа для добавления
//            if (documents.isNotEmpty()) {
//                var documentExpanded by remember { mutableStateOf(false) }
//
//                ExposedDropdownMenuBox(
//                    expanded = documentExpanded,
//                    onExpandedChange = { documentExpanded = !documentExpanded }
//                ) {
//                    TextField(
//                        value = selectedDocument?.number ?: "Выберите документ",
//                        onValueChange = {},
//                        readOnly = true,
//                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = documentExpanded) },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    ExposedDropdownMenu(
//                        expanded = documentExpanded,
//                        onDismissRequest = { documentExpanded = false }
//                    ) {
//                        documents.forEach { document ->
//                            DropdownMenuItem(
//                                text = { Text("Док. №${document.number}") },
//                                onClick = {
//                                    selectedDocument = document
//                                    documentExpanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Button(
//                    onClick = {
//                        selectedDocument?.let { document ->
//                            viewModel.addProductToDocument(document.id, product.barcode!!)
//                        }
//                    },
//                    enabled = selectedDocument != null
//                ) {
//                    Text("Добавить в документ")
//                }
//            } else {
//                Text("Нет доступных документов", color = Color.Gray)
//            }
//        }
//    }
//}
//
//@Composable
//fun DocumentsList(documents: List<Document>, viewModel: InventoryViewModel) {
//    LazyColumn {
//        items(documents.size) { i ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 4.dp),
//                onClick = {
//                    // Навигация к деталям документа
//                }
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text("Документ №${documents[i].number}", style = MaterialTheme.typography.titleMedium)
//                    Text("Склад: ${documents[i].warehouse?.name ?: "N/A"}")
//                    Text("Статус: ${documents[i].status}")
//                    Text("Товаров: ${documents[i].items.size}")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun HandleUiState(
//    uiState: InventoryUiState,
//    viewModel: InventoryViewModel,
//    onLoading: @Composable () -> Unit,
//    snackBarHostState: SnackbarHostState
//) {
//    LaunchedEffect(uiState.error) {
//        println("3")
//        uiState.error?.let {
//            println("error")
//            snackBarHostState.showSnackbar(uiState.error)
//        }
//    }
//
//    LaunchedEffect(uiState.successMessage) {
//        println("4")
//        uiState.successMessage?.let {
//            snackBarHostState.showSnackbar(uiState.successMessage.toString())
//            viewModel.clearError()
//        }
//    }
//
//    if (uiState.isLoading) {
//        println("5")
//        onLoading()
//    }
//}
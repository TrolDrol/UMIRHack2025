package com.az.umirhackapp.ui.screens.document_items

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.test.TestViewModel
import com.az.umirhackapp.test.documents
import com.az.umirhackapp.ui.dialogs.AddNewItemToDocumentDialog
import com.az.umirhackapp.ui.dialogs.CompleteDocumentDialog
import com.az.umirhackapp.ui.dialogs.NotExistProductDialog
import com.az.umirhackapp.ui.screens.CameraScanner
import com.az.umirhackapp.ui.screens.createDecoratedBarcodeView
import com.az.umirhackapp.ui.theme.AppTheme

@Composable
fun DocumentItemsScreen(
    viewModel: TestViewModel = viewModel(),
    selectedDocument: Document,
    onBackClick: () -> Unit,
    onNotPermissionCamera: () -> Unit,
    onCompleteDocument: () -> Unit,
    onCancelDocument: () -> Unit
) {
    var showCompletionDialog by remember { mutableStateOf(false) }
    var showAddNewItemToDocumentDialog by remember { mutableStateOf(false) }
    var showNotExistProductDialog by remember { mutableStateOf(false) }

    val scannedProduct by viewModel.scannedProduct.collectAsState()
    var documentItems = remember { mutableStateOf(selectedDocument.items) }
    var newDocumentItem = remember { mutableStateOf<DocumentItem?>(null) }

    val barcodeView = createDecoratedBarcodeView(LocalContext.current)
    val lastScannedCode = remember { mutableStateOf("") }

    LaunchedEffect(scannedProduct) {
        println("1. LaunchedEffect(${scannedProduct})")
        scannedProduct?.let { product ->
            println("2. LaunchedEffect: ${documentItems.value}")
            val itemIndex = documentItems.value.indexOfFirst { it.productId == product.id }
            if (itemIndex == -1) {
                newDocumentItem.value = DocumentItem(
                    0, selectedDocument.id, product.id, 0.0, 1.0, product
                )
                println("3. LaunchedEffect: ${newDocumentItem.value}")
                showAddNewItemToDocumentDialog = true
            } else {
                println("4. LaunchedEffect: ${documentItems.value}")
                val updatedItems = documentItems.value.toMutableList()
                val oldItem = updatedItems[itemIndex]

                val updatedItem = oldItem.copy(
                    product = product,
                    quantityActual = oldItem.quantityActual + 1.0
                )

                updatedItems[itemIndex] = updatedItem
                documentItems.value = updatedItems
                println("4. LaunchedEffect: ${documentItems.value}")
            }
        }
        viewModel.clearScannedProduct()
    }

    // Проверка разрешения камеры
    val cameraPermission = ContextCompat.checkSelfPermission(
        LocalContext.current, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    Box(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            if (cameraPermission) {
                CameraScanner(
                    barcodeView,
                    lastScannedCode,
                    { barcode ->
                        showNotExistProductDialog = viewModel.scanProduct(barcode)
                    }
                )
            } else {
                onNotPermissionCamera()
            }
            // Управление жизненным циклом сканера
            DisposableEffect(Unit) {
                barcodeView.resume()

                onDispose {
                    barcodeView.pause()
                }
            }
        }
        Scaffold(
            modifier = Modifier.alpha(0.5f),
            topBar = {
                DocumentItemsTopBar(
                    selectedDocument = selectedDocument,
                    onBackClick = onBackClick
                )
            },
            bottomBar = {
                DocumentActionsBottomBar(
                    selectedDocument = selectedDocument,
                    documentItems = documentItems.value,
                    onCompleteClick = { showCompletionDialog = true },
                    onCancelClick = onCancelDocument
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Статус документа и статистика
                DocumentHeader(selectedDocument, documentItems)

                // Список товаров
                DocumentItemsList(
                    documentItems = documentItems,
                    selectedDocument = selectedDocument
                )
            }

            // Диалог завершения документа
            if (showCompletionDialog) {
                CompleteDocumentDialog(
                    documentItems = documentItems.value,
                    onConfirm = {
                        onCompleteDocument()
                        showCompletionDialog = false
                    },
                    onDismiss = { showCompletionDialog = false }
                )
            }
            if (showAddNewItemToDocumentDialog) {
                AddNewItemToDocumentDialog(
                    newDocumentItem,
                    {
                        val newList = documentItems.value.toMutableList()
                        newList.add(newDocumentItem.value!!)
                        documentItems.value = newList
                        viewModel.addNewDocumentItem(newDocumentItem.value!!)
                        newDocumentItem.value = null
                        showAddNewItemToDocumentDialog = false
                    },
                    {
                        newDocumentItem.value = null
                        showAddNewItemToDocumentDialog = false
                    }
                )
            }

            if (showNotExistProductDialog) {
                NotExistProductDialog(
                    lastScannedCode.value,
                    { showNotExistProductDialog = false }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDocumentItemsScreen() {
    AppTheme {
    DocumentItemsScreen(
        selectedDocument = documents[1],
        onBackClick = {  },
        onNotPermissionCamera = {  },
        onCancelDocument = {  },
        onCompleteDocument = {  }
    )
        }
}
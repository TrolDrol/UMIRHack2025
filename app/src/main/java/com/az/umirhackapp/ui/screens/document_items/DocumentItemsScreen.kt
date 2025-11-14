package com.az.umirhackapp.ui.screens.document_items

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.az.umirhackapp.email.EmailViewModel
import com.az.umirhackapp.ui.dialogs.SendReportDialog
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.server.inventory.InventoryViewModel
import com.az.umirhackapp.test.documents
import com.az.umirhackapp.ui.dialogs.AddNewItemToDocumentDialog
import com.az.umirhackapp.ui.dialogs.CompleteDocumentDialog
import com.az.umirhackapp.ui.dialogs.NotExistProductDialog
import com.az.umirhackapp.ui.dialogs.OnBackClickDocumentItemsScreenDialog
import com.az.umirhackapp.ui.screens.Background
import com.az.umirhackapp.ui.screens.CameraScanner
import com.az.umirhackapp.ui.screens.createDecoratedBarcodeView
import com.az.umirhackapp.ui.theme.AppTheme

@SuppressLint("UnrememberedMutableState")
@Composable
fun DocumentItemsScreen(
    viewModel: InventoryViewModel = viewModel(),
    emailViewModel: EmailViewModel = viewModel(),
    selectedDocument: Document,
    onBackClick: () -> Unit,
    onStartInventory: (Document) -> Unit,
    onNotPermissionCamera: () -> Unit,
    onCompleteDocument: (Document, List<DocumentItem>) -> Unit,
    systemInDarkTheme: Boolean = true
) {
    val visibilityScaffold = remember { mutableFloatStateOf(1f) }

    var showCompletionDialog by remember { mutableStateOf(false) }
    var showAddNewItemToDocumentDialog by remember { mutableStateOf(false) }
    var showNotExistProductDialog by remember { mutableStateOf(false) }
    var showOnBackClickDocumentItemsScreenDialog by remember { mutableStateOf(false) }
    var showSendReportDialog by remember { mutableStateOf(false) }

    var selectedDocumentState by remember { mutableStateOf(selectedDocument) }
    val scannedProduct by viewModel.scannedProduct.collectAsState()
    var documentItems = remember { mutableStateOf(selectedDocumentState.items) }
    var newDocumentItem = remember { mutableStateOf<DocumentItem?>(null) }
    var newDocumentItems = remember { mutableStateOf(emptyList<DocumentItem>()) }

    val barcodeView = createDecoratedBarcodeView(LocalContext.current)
    val lastScannedCode = remember { mutableStateOf("") }

    var scanEnabled = remember { mutableStateOf(true) }

    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(scannedProduct) {
        scannedProduct?.let { product ->
            val itemIndex = documentItems.value.indexOfFirst { it.productId == product.id }
            if (itemIndex == -1) {
                newDocumentItem.value = DocumentItem(
                    0, selectedDocumentState.id, product.id, 0.0, 1.0, product
                )
                showAddNewItemToDocumentDialog = true
            } else {
                val updatedItems = documentItems.value.toMutableList()
                val oldItem = updatedItems[itemIndex]

                val updatedItem = oldItem.copy(
                    product = product,
                    quantityActual = oldItem.quantityActual + 1.0
                )

                updatedItems[itemIndex] = updatedItem
                documentItems.value = updatedItems
            }
        }
        viewModel.clearScannedProduct()
    }

    LaunchedEffect(uiState) {
        if (uiState.error != null) {
            snackBarHostState.showSnackbar("Error: ${uiState.error}")
        }
        if (uiState.successMessage != null) {
            snackBarHostState.showSnackbar("SuccessMessage: ${uiState.successMessage}")
        }
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
                    scanEnabled,
                    { barcode ->
                        println("DocumentItemsScreen: $barcode")
                        if (0.9 > visibilityScaffold.floatValue)
                            viewModel.scanProduct(
                                barcode,
                                {showNotExistProductDialog = true}
                            )
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
            snackbarHost = { SnackbarHost(snackBarHostState) },
            modifier = Modifier.alpha(visibilityScaffold.floatValue),
            topBar = {
                DocumentItemsTopBar(
                    selectedDocument = selectedDocumentState,
                    onBackClick = {
                        if (selectedDocumentState.status == "in_progress")
                            showOnBackClickDocumentItemsScreenDialog = true
                        else
                            onBackClick()
                    }
                )
            },
            bottomBar = {
                DocumentActionsBottomBar(
                    selectedDocument = selectedDocumentState,
                    documentItems = documentItems.value,
                    visibilityScaffold = visibilityScaffold,
                    onStartInventory = {document ->
                        onStartInventory(document)
                        selectedDocumentState = selectedDocumentState.copy(status = "in_progress")
                        println("selectedDocument: $selectedDocument")
                    },
                    onCompleteClick = { showCompletionDialog = true },
                    onSendReport = { showSendReportDialog = true }
                )
            }
        ) { paddingValues ->
            Background(systemInDarkTheme)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Статус документа и статистика
                DocumentHeader(selectedDocumentState, documentItems)

                // Список товаров
                DocumentItemsList(
                    documentItems = documentItems,
                    newDocumentItems = newDocumentItems,
                    selectedDocument = selectedDocumentState
                )
            }

            // Диалоги
            if (showCompletionDialog)
                CompleteDocumentDialog(
                    documentItems = documentItems.value,
                    onConfirm = {
                        onCompleteDocument(selectedDocumentState, documentItems.value)
                        showCompletionDialog = false
                    },
                    onDismiss = { showCompletionDialog = false }
                )

            if (showAddNewItemToDocumentDialog)
                AddNewItemToDocumentDialog(
                    newDocumentItem,
                    {
                        val newList = newDocumentItems.value.toMutableList()
                        newList.add(newDocumentItem.value!!)
                        newDocumentItems.value = newList
                        newDocumentItem.value = null
                        showAddNewItemToDocumentDialog = false
                    },
                    {
                        newDocumentItem.value = null
                        showAddNewItemToDocumentDialog = false
                    }
                )

            if (showNotExistProductDialog)
                NotExistProductDialog(
                    lastScannedCode.value,
                    { showNotExistProductDialog = false }
                )

            if (showOnBackClickDocumentItemsScreenDialog)
                OnBackClickDocumentItemsScreenDialog(
                    {
                        viewModel.updateDocumentItems(selectedDocument.id, documentItems.value, newDocumentItems.value)
                        showOnBackClickDocumentItemsScreenDialog = false
                        onBackClick()
                    },
                    {
                        showOnBackClickDocumentItemsScreenDialog = false
                    }
                )
            if (showSendReportDialog)
                SendReportDialog(
                    document = selectedDocument,
                    onDismiss = {
                        showSendReportDialog = false
                    },
                    onConfirm = { email, includePDF, includeCSV ->
                        emailViewModel.sendReport(selectedDocumentState, email, includePDF, includeCSV)
                        showSendReportDialog = false
                    }
                )
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
            onStartInventory = {  },
            onCompleteDocument = { document, list -> }
        )
        }
}
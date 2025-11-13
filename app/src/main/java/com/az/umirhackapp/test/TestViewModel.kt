package com.az.umirhackapp.test

import androidx.lifecycle.ViewModel
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.server.Organization
import com.az.umirhackapp.server.Product
import com.az.umirhackapp.server.Warehouse
import com.az.umirhackapp.server.inventory.InventoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestViewModel : ViewModel() {
    // Для демонстрации без реального API

    // Состояния UI
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState

    private val _scannedProduct = MutableStateFlow<Product?>(null)
    val scannedProduct: StateFlow<Product?> = _scannedProduct

    fun loadOrganizations() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        _uiState.value = _uiState.value.copy(
            organizations = organizations,
            selectedOrganization = organizations.firstOrNull()
        )
        _uiState.value = _uiState.value.copy(isLoading = false)
    }
    fun loadWarehouses(orgId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        _uiState.value = _uiState.value.copy(
            warehouses = getTestWarehousesByOrganization(orgId)
        )
        _uiState.value = _uiState.value.copy(isLoading = false)
    }
    fun scanProduct(barcode: String): Boolean {
        _uiState.value = _uiState.value.copy(isLoading = true)
        _scannedProduct.value = null

        // Поиск с очисткой
        val cleanBarcode = barcode.trim()
        val foundProduct = products.find { it.barcode == cleanBarcode }
        _scannedProduct.value = foundProduct
        println(foundProduct)

        _uiState.value = _uiState.value.copy(error = null)
        _uiState.value = _uiState.value.copy(isLoading = false)

        return _scannedProduct.value == null
    }

    fun loadDocuments(orgId: Int, warehouseId: Int? = null) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        _uiState.value = _uiState.value.copy(error = null, documents = getTestDocumentsByOrganization(orgId, warehouseId))

        _uiState.value = _uiState.value.copy(isLoading = false)
    }

    fun updateDocument(newDocument: Document) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        val oldDocuments = documents.toMutableList()
        val indexDocument = documents.indexOf(oldDocuments.find { it.number == newDocument.number })
        if (indexDocument != -1) {
            oldDocuments[indexDocument] = newDocument
            documents = oldDocuments.toList()
        }

        _uiState.value = _uiState.value.copy(isLoading = false)
    }

    fun addNewDocumentItem(item: DocumentItem) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        val newItemList = _uiState.value.selectDocument!!.items.toMutableList()

        val itemWithId = item.copy(id = documentItems.size + 1)
        newItemList.add(itemWithId)

        val newList = documentItems.toMutableList()
        newList.add(itemWithId)
        documentItems = newList

        _uiState.value.selectDocument?.items = newItemList

        _uiState.value = _uiState.value.copy(isLoading = false)
    }

    fun loadDocumentItem(orgId: Int, warehouseId: Int? = null, documentId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        _uiState.value = _uiState.value.copy(selectDocument = getTestDocument(orgId, warehouseId, documentId))

        _uiState.value = _uiState.value.copy(isLoading = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }

    fun selectOrganization(organization: Organization) {
        _uiState.value = _uiState.value.copy(selectedOrganization = organization)
        loadWarehouses(organization.id)
    }

    fun selectWarehouse(warehouse: Warehouse) {
        _uiState.value = _uiState.value.copy(selectedWarehouse = warehouse)
        loadDocuments(
            _uiState.value.selectedOrganization!!.id,
            _uiState.value.selectedWarehouse!!.id
        )
    }

    fun selectDocument(document: Document) {
        _uiState.value = _uiState.value.copy(selectDocument = document)
    }

    fun clearScannedProduct() {
        _scannedProduct.value = null
    }

    fun setError() {
        _uiState.value = _uiState.value.copy(error = "TEST ERROR")
    }
    fun setSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = "TEST successMessage")
    }

    fun clearSelectDocument() {
        _uiState.value = _uiState.value.copy(selectDocument = null)
    }

}

fun getTestWarehousesByOrganization(organizationId: Int): List<Warehouse> {
    return warehouses.filter { it.organizationId == organizationId }
}

fun getTestProductsByOrganization(organizationId: Int): List<Product> {
    return products.filter { it.organizationId == organizationId }
}

fun getTestDocumentsByOrganization(organizationId: Int, warehouseId: Int?): List<Document> {
    return documents.filter { it.organizationId == organizationId && it.warehouseId == warehouseId }
}

fun getTestProductByBarcode(barcode: String): Product? {
    return products.find { it.barcode == barcode }
}

fun getTestDocument(organizationId: Int, warehouseId: Int?, documentId: Int): Document {
    return documents.find {
        it.organizationId == organizationId && it.warehouseId == warehouseId && it.id == documentId
    }!!
}
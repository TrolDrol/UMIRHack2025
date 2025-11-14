package com.az.umirhackapp.server.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.server.Organization
import com.az.umirhackapp.server.Product
import com.az.umirhackapp.server.Result
import com.az.umirhackapp.server.Warehouse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InventoryViewModel(
    private val repository: InventoryRepository
) : ViewModel() {

    // Состояния UI
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState

    private val _scannedProduct = MutableStateFlow<Product?>(null)
    val scannedProduct: StateFlow<Product?> = _scannedProduct

    // Загрузка организаций и складов
    fun loadOrganizations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = repository.getOrganizations()) {
                is Result.Success -> {
                    val organizations = result.data.data!!
                    _uiState.value = _uiState.value.copy(
                        organizations = organizations
                    )
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(error = result.exception.message)
                }
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun loadWarehouses(organizationId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = repository.getWarehouses(organizationId)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(warehouses = result.data.data ?: emptyList())
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(error = result.exception.message)
                }
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    // Сканирование продукта
    fun scanProduct(barcode: String, onNotExistProduct: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = repository.getProductByBarcode(barcode)) {
                is Result.Success -> {
                    _scannedProduct.value = result.data.data
                    _uiState.value = _uiState.value.copy(error = null)
                }
                is Result.Failure -> {
                    _scannedProduct.value = null
                    _uiState.value = _uiState.value.copy(error = "Товар не найден: ${result.exception.message}")
                    onNotExistProduct()
                }
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    // Загрузка документов
    fun loadDocuments(organizationId: Int, warehouseId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = repository.getDocuments(organizationId, warehouseId)) {
                is Result.Success -> {
                    if (result.data.success)
                        _uiState.value = _uiState.value.copy(error = null, documents = result.data.data!!)
                    else
                        _uiState.value = _uiState.value.copy(error = result.data.error)
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(error = result.exception.message)
                }
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    // Создание нового документа
    fun createDocument(type: String, warehouseId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val documentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            when (val result = repository.createDocument(type, documentDate, warehouseId)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        error = null,
                        successMessage = "Документ создан"
                    )
                    loadDocuments(_uiState.value.selectedOrganization?.id ?: return@launch,
                        _uiState.value.selectedWarehouse?.id ?: return@launch)
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(error = result.exception.message)
                }
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun updateDocumentStatus(documentId: Int, status: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when(val result = repository.updateDocumentStatus(documentId, status)) {
                is Result.Success -> {
                    if (result.data.success) {
                        _uiState.value = _uiState.value.copy(error = null)
                        loadDocuments(
                            _uiState.value.selectedOrganization!!.id,
                            _uiState.value.selectedWarehouse!!.id
                        )
                    }
                    else
                        _uiState.value = _uiState.value.copy(error = result.data.error)
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(error = result.exception.message)
                }
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun updateDocumentItems(documentId: Int, oldItems: List<DocumentItem>, newItems: List<DocumentItem>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when(val result = repository.updateDocumentItems(documentId, oldItems, newItems)) {
                is Result.Success -> {
                    if (result.data.success) {
                        _uiState.value = _uiState.value.copy(error = null)
                        loadDocuments(
                            _uiState.value.selectedOrganization!!.id,
                            _uiState.value.selectedWarehouse!!.id
                        )
                    }
                    else
                        _uiState.value = _uiState.value.copy(error = result.data.error)
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(error = result.exception.message)
                }
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }

    fun clearScannedProduct() {
        _scannedProduct.value = null
    }

    fun selectOrganization(organization: Organization) {
        _uiState.value = _uiState.value.copy(selectedOrganization = organization)
    }

    fun selectWarehouse(warehouse: Warehouse) {
        _uiState.value = _uiState.value.copy(selectedWarehouse = warehouse)
    }

    fun selectDocument(document: Document) {
        _uiState.value = _uiState.value.copy(selectDocument = document)
    }
}

data class InventoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val organizations: List<Organization> = emptyList(),
    val warehouses: List<Warehouse> = emptyList(),
    val documents: List<Document> = emptyList(),
    val selectedOrganization: Organization? = null,
    val selectedWarehouse: Warehouse? = null,
    val selectDocument: Document? = null
)
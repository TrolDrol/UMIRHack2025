package com.az.umirhackapp.server.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.az.umirhackapp.server.Document
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
                        organizations = organizations,
                        selectedOrganization = organizations.firstOrNull()
                    )
                    // Автоматически загружаем склады для выбранной организации
                    result.data.data.firstOrNull()?.let { org ->
                        loadWarehouses(org.id)
                    }
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
    fun scanProduct(barcode: String) {
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
                }
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    // Загрузка документов
    fun loadDocuments(organizationId: Int, warehouseId: Int? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = repository.getDocuments(organizationId, warehouseId, "inventory")) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(error = null, documents = result.data.data!!)
                }
                is Result.Failure -> {
                    _uiState.value = _uiState.value.copy(error = result.exception.message)
                }
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    // Добавление продукта в документ
    fun addProductToDocument(documentId: Int, barcode: String, quantity: Double = 1.0) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = repository.addProductToDocument(documentId, barcode, quantity)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        error = null,
                        successMessage = "Товар добавлен в документ"
                    )
                    // Обновляем список документов
                    loadDocuments(_uiState.value.selectedOrganization?.id ?: return@launch)
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
                    loadDocuments(_uiState.value.selectedOrganization?.id ?: return@launch)
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

    fun selectOrganization(organization: Organization) {
        _uiState.value = _uiState.value.copy(selectedOrganization = organization)
        loadWarehouses(organization.id)
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
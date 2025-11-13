package com.az.umirhackapp.server

data class AuthRequest(
    val email: String,
    val password: String,
    val name: String? = null // только для регистрации
)

data class AuthResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String? = null,
    val user: User? = null
)

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
)

data class User(
    val id: Int,
    val email: String,
    val fullName: String,
    val createdAt: String?,
    val organization: List<Organization>?
)

data class Organization(
    val id: Int,
    val name: String,
    val inn: String?,
    val address: String?,
    val phone: String?,
    val userRole: String
)

data class Warehouse(
    val id: Int,
    val name: String,
    val address: String?,
    val organizationId: Int,
    val isActive: Boolean
)

data class Product(
    val id: Int,
    val name: String,
    val barcode: String?,
    val description: String?,
    val organizationId: Int
)

data class Document(
    val id: Int,
    val type: String,
    val number: String,
    val status: String, // 'draft', 'in_progress', 'completed', 'cancelled'
    val dateCreated: String,
    val documentDate: String,
    val organizationId: Int,
    val warehouseId: Int,
    val createdById: Int,
    val warehouse: Warehouse? = null,
    var items: List<DocumentItem> = listOf()
)

data class DocumentItem(
    val id: Int,
    val documentId: Int,
    val productId: Int,
    val quantityExpected: Double,
    var quantityActual: Double,
    var product: Product? = null
)

data class AddProductRequest(
    val documentId: Int,
    val productBarcode: String,
    val quantity: Double = 1.0
)

data class CreateDocumentRequest(
    val type: String,
    val documentDate: String,
    val warehouseId: Int
)
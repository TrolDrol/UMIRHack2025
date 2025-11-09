package com.az.umirhackapp.server.inventory

import com.az.umirhackapp.server.AddProductRequest
import com.az.umirhackapp.server.ApiResponse
import com.az.umirhackapp.server.ApiService
import com.az.umirhackapp.server.CreateDocumentRequest
import com.az.umirhackapp.server.Document
import com.az.umirhackapp.server.DocumentItem
import com.az.umirhackapp.server.Organization
import com.az.umirhackapp.server.Product
import com.az.umirhackapp.server.Warehouse
import com.az.umirhackapp.server.auth.TokenService
import retrofit2.Response
import com.az.umirhackapp.server.Result

class InventoryRepository(
    private val apiService: ApiService,
    private val tokenService: TokenService
) {
    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    // Продукты
    suspend fun getProductByBarcode(barcode: String): Result<ApiResponse<Product>> {
        val token = tokenService.getAuthToken() ?: return Result.Failure(Exception("No token"))
        return (safeApiCall { apiService.getProduct("Bearer $token", barcode) })
    }

    // Организации и склады
    suspend fun getOrganizations(): Result<ApiResponse<List<Organization>>> {
        val token = tokenService.getAuthToken() ?: return Result.Failure(Exception("No token"))
        return safeApiCall { apiService.getOrganizations("Bearer $token") }
    }

    suspend fun getWarehouses(organizationId: Int): Result<ApiResponse<List<Warehouse>>> {
        val token = tokenService.getAuthToken() ?: return Result.Failure(Exception("No token"))
        return safeApiCall { apiService.getWarehouses("Bearer $token", organizationId) }
    }

    // Документы
    suspend fun getDocuments(
        organizationId: Int,
        warehouseId: Int? = null,
        type: String? = null,
        status: String? = null
    ): Result<ApiResponse<List<Document>>> {
        val token = tokenService.getAuthToken() ?: return Result.Failure(Exception("No token"))
        return safeApiCall {
            apiService.getDocuments("Bearer $token", organizationId, warehouseId, type, status)
        }
    }

    suspend fun createDocument(type: String, documentDate: String, warehouseId: Int): Result<ApiResponse<Document>> {
        val token = tokenService.getAuthToken() ?: return Result.Failure(Exception("No token"))
        return safeApiCall {
            apiService.createDocument("Bearer $token", CreateDocumentRequest(type, documentDate, warehouseId))
        }
    }

    suspend fun addProductToDocument(documentId: Int, barcode: String, quantity: Double = 1.0): Result<ApiResponse<DocumentItem>> {
        val token = tokenService.getAuthToken() ?: return Result.Failure(Exception("No token"))
        return safeApiCall {
            apiService.addProductToDocument("Bearer $token", documentId, AddProductRequest(documentId, barcode, quantity))
        }
    }

    suspend fun updateDocumentStatus(documentId: Int, status: String): Result<ApiResponse<Document>> {
        val token = tokenService.getAuthToken() ?: return Result.Failure(Exception("No token"))
        return safeApiCall {
            apiService.updateDocumentStatus("Bearer $token", documentId, status)
        }
    }
}
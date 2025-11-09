package com.az.umirhackapp.server

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Аутентификация
    @POST("auth/register")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @GET("user/profile")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<ApiResponse<User>>

    // Продукты
    @GET("product/{barcode}")
    suspend fun getProduct(
        @Header("Authorization") token: String,
        @Path("barcode") barcode: String
    ): Response<ApiResponse<Product>>

    // Организации и склады
    @GET("organizations")
    suspend fun getOrganizations(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Organization>>>

    @GET("warehouses/{organizationId}")
    suspend fun getWarehouses(
        @Header("Authorization") token: String,
        @Path("organizationId") organizationId: Int
    ): Response<ApiResponse<List<Warehouse>>>

    // Документы
    @GET("documents")
    suspend fun getDocuments(
        @Header("Authorization") token: String,
        @Query("organizationId") organizationId: Int,
        @Query("warehouseId") warehouseId: Int? = null,
        @Query("type") type: String? = null,
        @Query("status") status: String? = null
    ): Response<ApiResponse<List<Document>>>

    @GET("documents/{documentId}")
    suspend fun getDocument(
        @Header("Authorization") token: String,
        @Path("documentId") documentId: Int
    ): Response<ApiResponse<Document>>

    @POST("documents")
    suspend fun createDocument(
        @Header("Authorization") token: String,
        @Body request: CreateDocumentRequest
    ): Response<ApiResponse<Document>>

    @POST("documents/{documentId}/add-product")
    suspend fun addProductToDocument(
        @Header("Authorization") token: String,
        @Path("documentId") documentId: Int,
        @Body request: AddProductRequest
    ): Response<ApiResponse<DocumentItem>>

    @PUT("documents/{documentId}/status")
    suspend fun updateDocumentStatus(
        @Header("Authorization") token: String,
        @Path("documentId") documentId: Int,
        @Body status: String
    ): Response<ApiResponse<Document>>
}
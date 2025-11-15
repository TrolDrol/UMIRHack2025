package com.az.umirhackapp.server.auth

import com.az.umirhackapp.server.ApiResponse
import com.az.umirhackapp.server.ApiService
import com.az.umirhackapp.server.AuthRequest
import com.az.umirhackapp.server.AuthResponse
import com.az.umirhackapp.server.JoinOrganizationRequest
import com.az.umirhackapp.server.Result
import com.az.umirhackapp.server.User
import retrofit2.Response

class AuthRepository(private val apiService: ApiService) {

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else
                Result.Failure(Exception("API Error: ${response.code()}"))
        } catch (e: Exception) {
            return Result.Failure(e)
        }
    }

    suspend fun register(email: String, password: String, name: String): Result<AuthResponse> {
        val request = AuthRequest(email = email, password = password, name = name)
        return safeApiCall { apiService.register(request) }
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        val request = AuthRequest(email = email, password = password)
        return safeApiCall { apiService.login(request) }
    }

    suspend fun getCurrentUser(token: String): Result<ApiResponse<User>> {
        return safeApiCall { apiService.getCurrentUser("Bearer $token") }
    }

    suspend fun invitationToOrganization(token: String, request: JoinOrganizationRequest): Result<ApiResponse<User>> {
        return safeApiCall { apiService.invitationToOrganization("Bearer $token", request) }
    }
}
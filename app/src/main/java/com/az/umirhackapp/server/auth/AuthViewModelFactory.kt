package com.az.umirhackapp.server.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.az.umirhackapp.server.auth.TokenService

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val tokenService: TokenService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository, tokenService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
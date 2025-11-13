package com.az.umirhackapp.server.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.az.umirhackapp.server.AuthResponse
import com.az.umirhackapp.server.Result
import com.az.umirhackapp.server.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val tokenService: TokenService
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(tokenService.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        // Если пользователь уже авторизован, загружаем его данные
        if (tokenService.isLoggedIn()) {
            loadCurrentUser()
        }
    }

    fun register(email: String, password: String, name: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            when (val result = authRepository.register(email, password, name)) {
                is Result.Success -> {
                    result.data.token?.let { token ->
                        tokenService.saveAuthToken(token)
                    }
                    result.data.user?.let { user ->
                        tokenService.saveUserId(user.id.toString())
                        _currentUser.value = user
                    }
                    _authState.value = AuthState.Success(result.data)
                    _isLoggedIn.value = true
                }
                is Result.Failure -> {
                    _authState.value = AuthState.Error(result.exception.message ?: "Registration failed")
                }
            }
        }
    }

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> {
                    result.data.token?.let { token ->
                        tokenService.saveAuthToken(token)
                    }
                    loadCurrentUser()
                    _authState.value = AuthState.Success(result.data)
                    _isLoggedIn.value = true
                }
                is Result.Failure -> {
                    _authState.value = AuthState.Error(result.exception.message ?: "Login failed")
                }
            }
        }
    }

    fun loadCurrentUser() {
        val token = tokenService.getAuthToken()
        if (token != null) {
            viewModelScope.launch {
                when (val result = authRepository.getCurrentUser(token)) {
                    is Result.Success -> {
                        _currentUser.value = result.data.data
                    }
                    is Result.Failure -> {
                        // Если ошибка 401 (Unauthorized), очищаем токен
                        if (result.exception.message?.contains("401") == true) {
                            logout()
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        tokenService.clearTokens()
        _currentUser.value = null
        _isLoggedIn.value = false
        _authState.value = AuthState.Idle
    }

    fun clearAuthState() {
        _authState.value = AuthState.Idle
    }
}

// Состояния аутентификации
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val data: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}
package com.az.umirhackapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.az.umirhackapp.server.NetworkModule
import com.az.umirhackapp.server.auth.AuthRepository
import com.az.umirhackapp.server.auth.AuthState
import com.az.umirhackapp.server.auth.AuthViewModel
import com.az.umirhackapp.server.auth.TokenService
import com.az.umirhackapp.ui.Screen
import com.az.umirhackapp.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    systemInDarkTheme: Boolean = true
) {
    val authState by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                onLoginSuccess()
            }
            is AuthState.Error -> {
                scope.launch {
                    snackBarHostState.showSnackbar((authState as AuthState.Error).message)
                }
                authViewModel.clearAuthState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        Background(systemInDarkTheme)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                // Заголовок
                Text(
                    text = Screen.LOGIN.title,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Поле для email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        Text(
                            text = "Email",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = email.isNotEmpty() && !email.contains("@")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Поле для пароля
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            text = "Пароль",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = password.isNotEmpty() && password.length < 6
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Кнопка входа или индикатор загрузки
                Button(
                    onClick = {
                        if (validateLogin(email, password)) {
                            authViewModel.login(email, password)
                        } else {
                            scope.launch {
                                snackBarHostState.showSnackbar("Пожалуйста, заполните все поля корректно")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = validateLogin(email, password) && authState !is AuthState.Loading,
                    content = {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Войти",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Ссылка на регистрацию
                TextButton(
                    onClick = onRegisterClick,
                    content = {
                        Text(
                            text = "Нет аккаунта? Зарегистрироваться",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }
    }
}

// Функция валидации данных для входа
private fun validateLogin(email: String, password: String): Boolean {
    return email.isNotBlank() && email.contains("@") && password.length >= 6
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    val authRepository = AuthRepository(NetworkModule.apiService)
    val tokenService = TokenService(LocalContext.current)
    val authViewModel: AuthViewModel = viewModel { AuthViewModel(authRepository, tokenService) }

    AppTheme {
        LoginScreen(
            authViewModel = authViewModel,
            onLoginSuccess = {
            },
            onRegisterClick = {
            }
        )
    }
}
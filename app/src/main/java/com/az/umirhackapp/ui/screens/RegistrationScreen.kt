package com.az.umirhackapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.server.auth.AuthState
import com.az.umirhackapp.server.auth.AuthViewModel
import com.az.umirhackapp.ui.Screen
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Обработка состояния аутентификации
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                onRegisterSuccess()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Заголовок
            Text(
                text = Screen.REGISTRATION.title,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Поле для имени
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(
                        text = "Имя",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Подтверждение пароля
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = {
                    Text(
                        text = "Подтвердите пароль",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка регистрации
            Button(
                onClick = {
                    if (validateRegistration(email, password, confirmPassword, name)) {
                        authViewModel.register(email, password, name)
                    } else {
                        scope.launch {
                            snackBarHostState.showSnackbar("Пожалуйста, заполните все поля корректно")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = validateRegistration(email, password, confirmPassword, name) &&
                        authState !is AuthState.Loading,
                content = {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = "Зарегистрироваться",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ссылка на вход
            TextButton(
                onClick = onLoginClick,
                content = {
                    Text(
                        text = "Уже есть аккаунт? Войти",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    }
}

// Функция валидации регистрационных данных
private fun validateRegistration(
    email: String,
    password: String,
    confirmPassword: String,
    name: String
): Boolean {
    return email.isNotBlank() &&
            password.length >= 6 &&
            password == confirmPassword &&
            name.isNotBlank()
}
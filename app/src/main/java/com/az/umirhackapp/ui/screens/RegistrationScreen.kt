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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.VisualTransformation
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
fun RegistrationScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }
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
        Background()
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
            OutlinedTextFieldDefault(
                name,
                {str -> name = str},
                "Имя",
                KeyboardOptions.Default,
                PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Поле для email
            OutlinedTextFieldDefault(
                email,
                {str -> email = str},
                "Email",
                KeyboardOptions(keyboardType = KeyboardType.Email),
                PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Поля для пароля и подтверждения
            PasswordFields(password, confirmPassword)

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка регистрации
            Button(
                onClick = {
                    if (validateRegistration(email, password.value, confirmPassword.value, name)) {
                        authViewModel.register(email, password.value, name)
                    } else {
                        scope.launch {
                            snackBarHostState.showSnackbar("Пожалуйста, заполните все поля корректно")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = validateRegistration(email, password.value, confirmPassword.value, name) &&
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
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
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

@Composable
fun PasswordFields(
    password: MutableState<String>,
    confirmPassword: MutableState<String>
) {
    OutlinedTextFieldDefault(
        password.value,
        {str -> password.value = str},
        "Пароль",
        KeyboardOptions(keyboardType = KeyboardType.Password),
        PasswordVisualTransformation()
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextFieldDefault(
        confirmPassword.value,
        {str -> confirmPassword.value = str},
        "Подтвердите пароль",
        KeyboardOptions(keyboardType = KeyboardType.Password),
        PasswordVisualTransformation()
    )
}

@Composable
fun OutlinedTextFieldDefault(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions,
    passwordVisualTransformation: PasswordVisualTransformation?
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = passwordVisualTransformation ?: VisualTransformation.None,
        keyboardOptions = keyboardOptions
    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewRegistrationScreen() {
    val authRepository = AuthRepository(NetworkModule.apiService)
    val tokenService = TokenService(LocalContext.current)
    val authViewModel: AuthViewModel = viewModel { AuthViewModel(authRepository, tokenService) }

    AppTheme {
        RegistrationScreen(
            authViewModel = authViewModel,
            onRegisterSuccess = {
            },
            onLoginClick = {
            }
        )
    }
}
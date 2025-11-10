package com.az.umirhackapp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.az.umirhackapp.server.NetworkModule
import com.az.umirhackapp.server.User
import com.az.umirhackapp.server.auth.AuthRepository
import com.az.umirhackapp.server.auth.AuthViewModel
import com.az.umirhackapp.server.auth.TokenService
import com.az.umirhackapp.server.inventory.InventoryRepository
import com.az.umirhackapp.server.inventory.InventoryViewModel
import com.az.umirhackapp.test.TestViewModel
import com.az.umirhackapp.ui.Screen
import com.az.umirhackapp.ui.screens.LazyColumnItems
import com.az.umirhackapp.ui.screens.LoadingScreen
import com.az.umirhackapp.ui.screens.LoginScreen
import com.az.umirhackapp.ui.screens.MainScreen
import com.az.umirhackapp.ui.screens.PermissionRequestScreen
import com.az.umirhackapp.ui.screens.ProfileScreen
import com.az.umirhackapp.ui.screens.QRScannerScreen
import com.az.umirhackapp.ui.screens.RegistrationScreen
import com.az.umirhackapp.ui.screens.SettingsScreen
import com.az.umirhackapp.ui.screens.document_items.DocumentItemsScreen
import com.az.umirhackapp.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val authRepository = AuthRepository(NetworkModule.apiService)
    val tokenService = TokenService(context)
    val authViewModel: AuthViewModel = viewModel { AuthViewModel(authRepository, tokenService) }

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val inventoryRepository = InventoryRepository(NetworkModule.apiService, tokenService)
    val inventoryViewModel = viewModel { InventoryViewModel(inventoryRepository) }

    val uiState by inventoryViewModel.uiState.collectAsState()

    // TODO("")
    val testViewModel = viewModel { TestViewModel() }

    val permissionPrefs = remember  {
        context.getSharedPreferences("Permissions", MODE_PRIVATE)
    }

    val hasPermissions by remember {
        mutableStateOf(permissionPrefs.getBoolean("check", false))
    }

    val navController = rememberNavController()

//    // TODO("")
//    // Обработка навигации в зависимости от состояния авторизации
//    LaunchedEffect(isLoggedIn, hasPermissions) {
//        if (!hasPermissions) {
//            navController.navigate(Screen.PERMISSION_REQUEST.route) {
//                popUpTo(Screen.LOADING.route) { inclusive = true }
//            }
//            return@LaunchedEffect
//        }
//
//        if (isLoggedIn)
//            // Если пользователь авторизован, переходим на главный экран
//            navController.navigate(Screen.MAIN.route) {
//                popUpTo(Screen.LOADING.route) { inclusive = true }
//            }
//        else
//            // Если не авторизован, переходим на экран регистрации
//            navController.navigate(Screen.REGISTRATION.route) {
//                popUpTo(Screen.LOADING.route) { inclusive = true }
//            }
//    }

    NavHost(
        navController = navController,
//        TODO("Screen.LOADING.route")
        startDestination = Screen.MAIN_ORGANIZATIONS.route
    ) {
        // Экран загрузки
        composable(Screen.LOADING.route) {
            LoadingScreen(
                message = "Загружаем приложение...",
                subMessage = "Пожалуйста, подождите"
            )
        }

        // Экран регистрации
        composable(Screen.REGISTRATION.route) {
            RegistrationScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.MAIN.route) {
                        popUpTo(Screen.REGISTRATION.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.LOGIN.route)
                }
            )
        }

        // Экран входа
        composable(Screen.LOGIN.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    // Успешный вход - переходим на главный экран
                    navController.navigate(Screen.MAIN.route) {
                        popUpTo(Screen.LOGIN.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.REGISTRATION.route)
                }
            )
        }

        // Главный экран
        composable(Screen.MAIN_ORGANIZATIONS.route) {
            MainScreen(
                authViewModel.currentUser.value ?: User(0, "example@com", "Иванов Иван", "+71231231212"),
                label = Screen.MAIN_ORGANIZATIONS.title,
                onProfileClick = {
                    navController.navigate(Screen.PROFILE.route)
                },
                onNotificationClick = {
                    // В будущем можно добавить экран уведомлений
                },
                onQRScannerClick = {
                    navController.navigate(Screen.QR_SCANNER.route)
                },
                onBackClick = { navController.popBackStack() },
                loadContent = {
                    println(testViewModel.uiState.value.organizations)
                    testViewModel.loadOrganizations()
                    println(testViewModel.uiState.value.organizations)
                              },
                content = {
                    LazyColumnItems(
                        testViewModel.uiState.collectAsState().value.organizations,
                        { org ->
                            println(testViewModel.uiState.value.selectedOrganization)
                            testViewModel.selectOrganization(org)
                            println(testViewModel.uiState.value.selectedOrganization)
                            navController.navigate(Screen.MAIN_WAREHOUSES.route)
                        },
                        Screen.MAIN_ORGANIZATIONS
                    )
                }
            )
        }

        composable(Screen.MAIN_WAREHOUSES.route) {
            MainScreen(
                authViewModel.currentUser.value ?: User(0, "example@com", "Иванов Иван", "+71231231212"),
                label = Screen.MAIN_WAREHOUSES.title,
                onProfileClick = {
                    navController.navigate(Screen.PROFILE.route)
                },
                onNotificationClick = {
                    // В будущем можно добавить экран уведомлений
                },
                onQRScannerClick = {
                    navController.navigate(Screen.QR_SCANNER.route)
                },
                onBackClick = { navController.popBackStack() },
                loadContent = {
                    println(testViewModel.uiState.value.organizations)
                    testViewModel.loadWarehouses(testViewModel.uiState.value.selectedOrganization!!.id)
                    println(testViewModel.uiState.value.organizations)
                },
                content = {
                    LazyColumnItems(
                        testViewModel.uiState.collectAsState().value.warehouses,
                        { warehouse ->
                            println(testViewModel.uiState.value.selectedWarehouse)
                            testViewModel.selectWarehouse(warehouse)
                            println(testViewModel.uiState.value.selectedWarehouse)
                            navController.navigate(Screen.MAIN_DOCUMENTS.route)
                        },
                        Screen.MAIN_WAREHOUSES
                    )
                }
            )
        }

        composable(Screen.MAIN_DOCUMENTS.route) {
            MainScreen(
                authViewModel.currentUser.value ?: User(0, "example@com", "Иванов Иван", "+71231231212"),
                label = Screen.MAIN_DOCUMENTS.title,
                onProfileClick = {
                    navController.navigate(Screen.PROFILE.route)
                },
                onNotificationClick = {
                    // В будущем можно добавить экран уведомлений
                },
                onQRScannerClick = {
                    navController.navigate(Screen.QR_SCANNER.route)
                },
                onBackClick = { navController.popBackStack() },
                loadContent = {
                    testViewModel.loadDocuments(
                        testViewModel.uiState.value.selectedOrganization!!.id,
                        testViewModel.uiState.value.selectedWarehouse!!.id
                    )
                },
                content = {
                    LazyColumnItems(
                        items = testViewModel.uiState.collectAsState().value.documents,
                        { document ->
                            testViewModel.selectDocument(document)
                            navController.navigate(Screen.MAIN_DOCUMENT_ITEMS.route)
                        },
                        Screen.MAIN_DOCUMENTS
                    )
                }
            )
        }

        composable(Screen.MAIN_DOCUMENT_ITEMS.route) {
            DocumentItemsScreen(
                viewModel = testViewModel,
                selectedDocument = testViewModel.uiState.collectAsState().value.selectDocument!!,
                onBackClick = { navController.popBackStack() },
                onNotPermissionCamera = { navController.navigate(Screen.PERMISSION_REQUEST.route) },
                onCancelDocument = {  },
                onCompleteDocument = { navController.popBackStack() }
            )
        }

        // Экран профиля
        composable(Screen.PROFILE.route) {
            ProfileScreen(
                authViewModel = authViewModel,
                onSettingsClick = {
                    navController.navigate(Screen.SETTINGS.route)
                },
                onEditProfileClick = {
                    // В будущем можно добавить экран редактирования профиля
                },
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.REGISTRATION.route) {
                        popUpTo(Screen.MAIN.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Экран настроек
        composable(Screen.SETTINGS.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLanguageClick = {
                    // В будущем можно добавить экран выбора языка
                },
                onPrivacyClick = {
                    // В будущем можно добавить экран конфиденциальности
                },
                onStorageClick = {
                    // В будущем можно добавить экран управления хранилищем
                }
            )
        }

        composable(Screen.QR_SCANNER.route) {
            QRScannerScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCodeScanned = { barcode ->
                    Log.i("onCodeScanned", barcode)
                    testViewModel.scanProduct(barcode)
                },
                onNotPermissionCamera = {
                    navController.navigate(Screen.PERMISSION_REQUEST.route)
                }
            )
        }

        composable(Screen.PERMISSION_REQUEST.route) {
            PermissionRequestScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPermissionsGranted = {
                    permissionPrefs.edit { putBoolean("check", true) }
                    navController.navigate(
                        Screen.MAIN.route
//                        TODO("if (isLoggedIn) Screen.MAIN.route else Screen.REGISTRATION.route")
                    ) {
                        popUpTo(Screen.PERMISSION_REQUEST.route) { inclusive = true }
                    }
                },
                onPermissionsDenied = {

                }
            )
        }
    }
}

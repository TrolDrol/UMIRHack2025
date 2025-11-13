package com.az.umirhackapp.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.az.umirhackapp.server.NetworkModule
import com.az.umirhackapp.server.auth.TokenService
import com.az.umirhackapp.server.inventory.InventoryRepository
import com.az.umirhackapp.server.inventory.InventoryViewModel
import com.az.umirhackapp.ui.Screen
import com.az.umirhackapp.ui.theme.AppTheme
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeView
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QRScannerScreen(
    onBackClick: () -> Unit,
    onCodeScanned: (String) -> Unit,
    onNotPermissionCamera: () -> Unit
) {
    val context = LocalContext.current

    // Проверка разрешения камеры
    val cameraPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    if (cameraPermission) {
        ScannerContent(
            onBackClick = onBackClick,
            onCodeScanned = onCodeScanned
        )
    } else {
        onNotPermissionCamera()
    }
}

@Composable
fun ScannerContent(
    onBackClick: () -> Unit,
    onCodeScanned: (String) -> Unit
) {
    var lastScannedCode = remember { mutableStateOf("") }
    val barcodeView = createDecoratedBarcodeView(LocalContext.current)
    var scanEnabled = remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraScanner(
            barcodeView,
            lastScannedCode,
            scanEnabled,
            onCodeScanned
        )
        TopBarQRScanner(
            onBackClick,
            Modifier.align(Alignment.TopCenter)
        )
        BottomBarQRScanner(
            barcodeView,
            lastScannedCode.value,
            Modifier.align(Alignment.BottomCenter)
        )
    }

    // Управление жизненным циклом сканера
    DisposableEffect(Unit) {
        barcodeView.resume()

        onDispose {
            barcodeView.pause()
            // Выключаем фонарик при выходе
            toggleTorch(barcodeView.barcodeView, false)
        }
    }
}

@Composable
fun TopBarQRScanner(
    onBackClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.5f))
            .then(modifier)
            .height(78.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            content = {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }
        )
        Text(
            Screen.QR_SCANNER.title,
            modifier = Modifier.padding(top = 7.dp),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
    }
}

@Composable
fun BottomBarQRScanner(
    barcodeView: DecoratedBarcodeView,
    lastScannedCode: String,
    modifier: Modifier
) {
    var torchEnabled by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Кнопка фонарика
        IconButton(
            onClick = {
                torchEnabled = !torchEnabled
                toggleTorch(barcodeView.barcodeView, torchEnabled)
            },
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = if (torchEnabled) Icons.Default.FlashlightOn else Icons.Default.FlashlightOff,
                contentDescription = if (torchEnabled) "Выключить фонарик" else "Включить фонарик",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (torchEnabled) "Фонарик включен" else "Включить фонарик",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Инструкция
        Text(
            text = "Наведите камеру на QR или штрих-код",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )

        // Последний отсканированный код
        if (lastScannedCode.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Сканировано: ${lastScannedCode.take(30)}...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

// Функция для управления фонариком
fun toggleTorch(barcodeView: BarcodeView, enable: Boolean) {
    try {
        barcodeView.setTorch(enable)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun CameraScanner(
    barcodeView: DecoratedBarcodeView,
    lastScannedCode: MutableState<String>,
    scanEnabled: MutableState<Boolean>,
    onCodeScanned: (String) -> Unit
) {
    // Камера для сканирования
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                barcodeView.apply {
                    // Обработка сканированных кодов
                    decodeContinuous { result ->
                        result?.text?.let { scannedText ->
                            if (scanEnabled.value) {
                                println(scanEnabled.toString())
                                lastScannedCode.value = scannedText
                                scanEnabled.value = false

                                // Вызываем колбэк с отсканированным кодом
                                onCodeScanned(scannedText)
                                CoroutineScope(Dispatchers.IO).launch {
                                    delay(2000)
                                    scanEnabled.value = true
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Референс на BarcodeView для управления сканером
@Composable
fun createDecoratedBarcodeView(context: Context): DecoratedBarcodeView {

    val barcodeView = DecoratedBarcodeView(context)
    barcodeView.decoderFactory.apply {
        DefaultDecoderFactory(
            listOf(
                BarcodeFormat.QR_CODE,
                BarcodeFormat.CODE_128,
                BarcodeFormat.CODE_39,
                BarcodeFormat.EAN_13,
                BarcodeFormat.EAN_8,
                BarcodeFormat.UPC_A,
                BarcodeFormat.UPC_E
            )
        )
    }
    barcodeView.setStatusText("")
    return barcodeView
}

@Preview(showSystemUi = true)
@Composable
fun PreviewQRScannerScreen() {
    val tokenService = TokenService(LocalContext.current)
    val inventoryRepository = InventoryRepository(NetworkModule.apiService, tokenService)
    val inventoryViewModel = viewModel { InventoryViewModel(inventoryRepository) }

    AppTheme {
        QRScannerScreen(
            onBackClick = {
            },
            onCodeScanned = { barcode ->
                inventoryViewModel.scanProduct(barcode, {  })
            },
            onNotPermissionCamera = {
            }
        )
    }
}
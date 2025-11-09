package com.az.umirhackapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun PermissionRequestScreen(
    onBackClick: () -> Unit,
    onPermissionsGranted: () -> Unit,
    onPermissionsDenied: () -> Unit
) {
    val context = LocalContext.current

    // Список разрешений для запроса
    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        //Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        val allGranted = map.values.all { it }
        if (allGranted) {
            onPermissionsGranted()
        } else {
            onPermissionsDenied()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Доступ к камере",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Для сканирования QR-кодов и штрих-кодов необходимо предоставить доступ к камере",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = {
                val hasAllPermissions = permissions.all { permission ->
                    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
                }
                if (hasAllPermissions) {
                    onPermissionsGranted()
                } else {
                    permissionLauncher.launch(permissions)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Предоставить доступ")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBackClick) {
            Text("Назад")
        }
    }
}
package com.az.umirhackapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

//// Обычная тема
//// Основные цвета
//val Purple80 = Color(0xFFD0BCFF)
//val PurpleGrey80 = Color(0xFFCCC2DC)
//val Pink80 = Color(0xFFEFB8C8)
//
//val Purple40 = Color(0xFF6650a4)
//val PurpleGrey40 = Color(0xFF625b71)
//val Pink40 = Color(0xFF7D5260)
//
//// Ваши текущие цвета + расширенные
//val purple_200 = Color(0xFFBB86FC)
//val purple_500 = Color(0xFF6200EE)
//val purple_700 = Color(0xFF3700B3)
//val pink_200 = Color(0xFFFF7597)
//
//// Новые расширенные цвета для более богатой палитры
//val deepPurple_100 = Color(0xFFD1C4E9)
//val deepPurple_300 = Color(0xFF9575CD)
//val deepPurple_700 = Color(0xFF512DA8)
//
//val teal_200 = Color(0xFF03DAC5)
//val teal_700 = Color(0xFF018786)
//
//val surfaceLight = Color(0xFFFFFBFE)
//val surfaceDark = Color(0xFF1C1B1F)
//val surfaceVariantLight = Color(0xFFE7E0EC)
//val surfaceVariantDark = Color(0xFF49454F)
//
//// Градиентные цвета
//val gradientStart = Color(0xFF667EEA)
//val gradientEnd = Color(0xFF764BA2)
//val gradientPink = Color(0xFFFF5EBC)

// Голубо-сине-черная тема
// Основная сине-голубая палитра
val blue_50 = Color(0xFFE3F2FD)
val blue_100 = Color(0xFFBBDEFB)
val blue_200 = Color(0xFF90CAF9)
val blue_300 = Color(0xFF64B5F6)
val blue_400 = Color(0xFF42A5F5)
val blue_500 = Color(0xFF2196F3)
val blue_600 = Color(0xFF1E88E5)
val blue_700 = Color(0xFF1976D2)
val blue_800 = Color(0xFF1565C0)
val blue_900 = Color(0xFF0D47A1)

// Акцентные голубые цвета
val cyan_200 = Color(0xFF80DEEA)
val cyan_400 = Color(0xFF26C6DA)
val cyan_700 = Color(0xFF00ACC1)
val cyan_900 = Color(0xFF006064)

// Темные/черные тона
val dark_900 = Color(0xFF000000)
val dark_800 = Color(0xFF121212)
val dark_700 = Color(0xFF1E1E1E)
val dark_600 = Color(0xFF2D2D2D)

// Светлые тона
val light_50 = Color(0xFFFAFAFA)
val light_100 = Color(0xFFF5F5F5)
val light_200 = Color(0xFFEEEEEE)

// Градиенты
val gradientBlueStart = Color(0xFF2196F3)
val gradientBlueEnd = Color(0xFF00ACC1)
val gradientDarkStart = Color(0xFF0D47A1)
val gradientDarkEnd = Color(0xFF006064)

// Дополнительные цвета
val surfaceLight = Color(0xFFFFFFFF)
val surfaceDark = Color(0xFF121212)
val surfaceVariantLight = Color(0xFFE3F2FD)
val surfaceVariantDark = Color(0xFF1E1E1E)

// Системные цвета
val errorRed = Color(0xFFCF6679)
val successGreen = Color(0xFF4CAF50)
val warningAmber = Color(0xFFFFC107)

// Градиентный фон
@Composable
fun getGradientBrush(): Brush {
    return Brush.verticalGradient(
        colors = if (isSystemInDarkTheme()) {
            listOf(
                MaterialTheme.colorScheme.background,
                dark_700,
                dark_800
            )
        } else {
            listOf(
                MaterialTheme.colorScheme.background,
                blue_50,
                MaterialTheme.colorScheme.background
            )
        }
    )
}
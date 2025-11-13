package com.az.umirhackapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

//// Обычная тема
//private val LightColorScheme = lightColorScheme(
//    primary = purple_500,
//    onPrimary = Color.White,
//    primaryContainer = deepPurple_100,
//    onPrimaryContainer = deepPurple_700,
//
//    secondary = teal_700,
//    onSecondary = Color.White,
//    secondaryContainer = teal_200,
//    onSecondaryContainer = teal_700,
//
//    tertiary = pink_200,
//    onTertiary = Color.White,
//    tertiaryContainer = Color(0xFFFFD8E4),
//    onTertiaryContainer = Color(0xFF8A4A5C),
//
//    background = surfaceLight,
//    onBackground = Color(0xFF1C1B1F),
//    surface = surfaceLight,
//    onSurface = Color(0xFF1C1B1F),
//    surfaceVariant = surfaceVariantLight,
//    onSurfaceVariant = Color(0xFF49454F),
//
//    error = Color(0xFFBA1A1A),
//    onError = Color.White
//)
//
//private val DarkColorScheme = darkColorScheme(
//    primary = purple_200,
//    onPrimary = Color.Black,
//    primaryContainer = deepPurple_700,
//    onPrimaryContainer = deepPurple_100,
//
//    secondary = teal_200,
//    onSecondary = Color.Black,
//    secondaryContainer = teal_700,
//    onSecondaryContainer = teal_200,
//
//    tertiary = pink_200,
//    onTertiary = Color.Black,
//    tertiaryContainer = Color(0xFF8A4A5C),
//    onTertiaryContainer = Color(0xFFFFD8E4),
//
//    background = surfaceDark,
//    onBackground = Color(0xFFE6E1E5),
//    surface = surfaceDark,
//    onSurface = Color(0xFFE6E1E5),
//    surfaceVariant = surfaceVariantDark,
//    onSurfaceVariant = Color(0xFFCAC4D0),
//
//    error = Color(0xFFFFB4AB),
//    onError = Color.Black
//)

// Голубо-сине-черная тема
private val LightColorScheme = lightColorScheme(
    primary = blue_700,
    onPrimary = Color.White,
    primaryContainer = blue_100,
    onPrimaryContainer = blue_900,

    secondary = cyan_700,
    onSecondary = Color.White,
    secondaryContainer = cyan_200,
    onSecondaryContainer = cyan_900,

    tertiary = blue_400,
    onTertiary = Color.White,
    tertiaryContainer = blue_50,
    onTertiaryContainer = blue_800,

    background = light_50,
    onBackground = dark_900,
    surface = surfaceLight,
    onSurface = dark_900,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = blue_900,

    outline = blue_300,
    outlineVariant = blue_100,

    error = errorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

private val DarkColorScheme = darkColorScheme(
    primary = blue_300,
    onPrimary = dark_900,
    primaryContainer = blue_800,
    onPrimaryContainer = blue_100,

    secondary = cyan_200,
    onSecondary = dark_900,
    secondaryContainer = cyan_900,
    onSecondaryContainer = cyan_200,

    tertiary = blue_200,
    onTertiary = dark_900,
    tertiaryContainer = blue_900,
    onTertiaryContainer = blue_100,

    background = dark_800,
    onBackground = light_100,
    surface = surfaceDark,
    onSurface = light_100,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = blue_100,

    outline = blue_600,
    outlineVariant = blue_800,

    error = errorRed,
    onError = dark_900,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primaryContainer.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme

            // Навигационная панель
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypographyBlue,
        content = content
    )
}
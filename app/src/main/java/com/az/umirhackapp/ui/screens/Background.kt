package com.az.umirhackapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.az.umirhackapp.ui.theme.AppTheme
import com.az.umirhackapp.ui.theme.blue_100
import com.az.umirhackapp.ui.theme.blue_300
import com.az.umirhackapp.ui.theme.blue_700
import com.az.umirhackapp.ui.theme.blue_900
import com.az.umirhackapp.ui.theme.cyan_200
import com.az.umirhackapp.ui.theme.dark_700
import com.az.umirhackapp.ui.theme.dark_800

@Composable
fun Background(
    systemInDarkTheme: Boolean
) {
    // Красивый градиентный фон
    val backgroundGradient = if (systemInDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                blue_900,
                dark_800,
                dark_700
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE3F2FD),
                Color(0xFFBBDEFB),
                Color(0xFF90CAF9),
                Color(0xFF64B5F6)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        if (!systemInDarkTheme) {
            // Большой голубой круг
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(300.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(blue_100, Color.Transparent),
                            radius = 300f
                        )
                    )
            )

            // Синий круг справа
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(200.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(cyan_200, Color.Transparent),
                            radius = 200f
                        )
                    )
            )

            // Маленький круг внизу
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(150.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(blue_300, Color.Transparent),
                            radius = 150f
                        )
                    )
            )
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(300.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(blue_700, Color.Transparent),
                            radius = 250f
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(200.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(blue_900, Color.Transparent),
                            radius = 180f
                        )
                    )
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewBackground() {
    val systemInDarkTheme = remember { mutableStateOf(true) }
    AppTheme {
        Background(systemInDarkTheme.value)
    }
}
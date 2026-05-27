package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NeoBrutalistColorScheme = lightColorScheme(
    primary = NeoYellow,
    secondary = NeoOrange,
    tertiary = NeoCyan,
    background = NeoBg,
    surface = CardBg,
    onPrimary = NeoBlack,
    onSecondary = NeoBlack,
    onTertiary = NeoBlack,
    onBackground = NeoBlack,
    onSurface = NeoBlack
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NeoBrutalistColorScheme,
        typography = Typography,
        content = content
    )
}

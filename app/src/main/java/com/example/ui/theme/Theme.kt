package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AccentGold,
    onPrimary = Color.White,
    primaryContainer = AccentGoldLight,
    onPrimaryContainer = TextMain,
    secondary = AccentGoldDark,
    onSecondary = Color.White,
    background = BgColor,
    onBackground = TextMain,
    surface = CardBg,
    onSurface = TextMain,
    surfaceVariant = TrackBg,
    onSurfaceVariant = TextMuted,
    outline = BorderColor
)

private val DarkColorScheme = darkColorScheme(
    primary = AccentGold,
    onPrimary = Color.Black,
    primaryContainer = AccentGoldDark,
    onPrimaryContainer = Color.White,
    secondary = AccentGoldLight,
    onSecondary = Color.Black,
    background = Color(0xFF1C1A17),
    onBackground = Color(0xFFEDE8DF),
    surface = Color(0xFF26231E),
    onSurface = Color(0xFFEDE8DF),
    surfaceVariant = Color(0xFF38342D),
    onSurfaceVariant = Color(0xFFB5ADA2),
    outline = Color(0xFF4A443A)
)

@Composable
fun AswinBreathTheme(
    darkTheme: Boolean = false, // Preserve the serene warm aesthetic by default
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

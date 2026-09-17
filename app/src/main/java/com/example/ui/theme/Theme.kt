package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimary,
    onPrimary = Color.White,
    primaryContainer = BrandPrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = BrandPrimaryLight,
    onSecondary = Color.White,
    background = BrandStructural,
    onBackground = BrandSurface,
    surface = BrandStructuralVariant,
    onSurface = BrandSurface,
    surfaceVariant = BrandStructuralCard,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF334155),
    error = DangerRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary, // #FF521D
    onPrimary = Color.White,
    primaryContainer = BrandPrimaryContainer,
    onPrimaryContainer = Color(0xFF7C1D06),
    secondary = BrandStructural, // #0B0F19
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE2E8F0),
    onSecondaryContainer = BrandStructural,
    background = BrandSurface, // #F1F5F9
    onBackground = BrandStructural, // #0B0F19
    surface = Color.White,
    onSurface = BrandStructural,
    surfaceVariant = BrandSurfaceVariant,
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    error = DangerRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set to false by default so our custom brand identity #FF521D / #0B0F19 is always prominently displayed
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


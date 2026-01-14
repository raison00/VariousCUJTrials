package com.homehub.auto.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HomeHubColorScheme = darkColorScheme(
    primary = Purple500,
    onPrimary = Color.White,
    primaryContainer = Purple700,
    onPrimaryContainer = Color.White,
    
    secondary = Turquoise500,
    onSecondary = Color.Black,
    secondaryContainer = Turquoise700,
    onSecondaryContainer = Color.White,
    
    tertiary = Turquoise300,
    onTertiary = Color.Black,
    
    background = Purple900,
    onBackground = Color.White,
    
    surface = Purple800,
    onSurface = Color.White,
    surfaceVariant = Purple700,
    onSurfaceVariant = Color.White,
    
    error = StatusError,
    onError = Color.White
)

@Composable
fun HomeHubAutoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HomeHubColorScheme,
        content = content
    )
}

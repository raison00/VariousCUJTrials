package com.uxcompose.tic_tac_toe_test.ui.theme


import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
//import androidx.tv.material3.ColorScheme
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.compose.material3.darkColorScheme as m3DarkColorScheme
import androidx.compose.material3.lightColorScheme as m3LightColorScheme
import androidx.compose.material3.MaterialTheme as M3MaterialTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AppTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    // Add a new parameter to enable/disable dynamic color
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isInDarkTheme) m3DarkColorScheme() else m3LightColorScheme() // Simplified, as dynamic colors from M3 are the goal
        }
        isInDarkTheme -> m3DarkColorScheme(
            primary = BoardSurface,
            secondary = PurpleGrey80,
            tertiary = Pink80,
            surface = DarkGray
        )
        else -> m3LightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40
        )
    }

}


@Composable
fun TicTacToeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Dynamic theme selection
    content: @Composable () -> Unit
) {

    M3MaterialTheme(
        colorScheme = if (darkTheme) m3DarkColorScheme() else m3LightColorScheme(),
        content = content
    )
}
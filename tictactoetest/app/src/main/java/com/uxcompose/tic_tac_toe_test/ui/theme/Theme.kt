package com.uxcompose.tic_tac_toe_test.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.tv.material3.ColorScheme
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TictactoetestTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,

) {
    val colorScheme = if (isInDarkTheme) {
        darkColorScheme(
            primary = Purple80,
            secondary = PurpleGrey80,
            tertiary = Pink80
        )
    } else {
        lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40
        )
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun TicTacToeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Dynamic theme selection
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        content = content
    )
}
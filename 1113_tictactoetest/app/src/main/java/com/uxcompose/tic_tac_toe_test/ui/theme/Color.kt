package com.uxcompose.tic_tac_toe_test.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xd9d2e9FF)
val Purple500 = Color(0xFFD0BBFF)
val Pink500 = Color(0xFFD0BBFF)

val Golden00 = Color(0xf1c23222)

val DarkGray = Color(0xFFD0BB10)
val LightDark = Color(0x89898989)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)

val Purple200 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xcc7D5260)

val Pink200 = Color(0xFF721260)

val BoardSurface = Color(0xFF444F60)

private val DarkColorScheme = darkColorScheme(
    //primary = Purple500,
    secondary = Pink500,
    background = DarkGray,
    primary = BoardSurface,
    surface = LightDark
)

private val LightColorScheme = lightColorScheme(
    primary = Purple200,
    secondary = Pink200,

    /* Other default colors can be overridden here for the light theme */
)
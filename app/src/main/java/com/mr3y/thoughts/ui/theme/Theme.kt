package com.mr3y.thoughts.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalTheme = staticCompositionLocalOf<Colors> {
    error("You must provide initial value for a theme before Using it")
}

private val DarkColorPalette = darkColors(
    primary = purple200,
    primaryVariant = purple700,
    secondary = teal200
)

private val LightColorPalette = lightColors(
    primary = purple500,
    primaryVariant = purple700,
    secondary = teal200

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ThoughtsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(LocalTheme provides colors) {
        MaterialTheme(
            colors = LocalTheme.current,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

/**
 * Theme Object to hold read-only properties of our theme such as:
 * colors, elevation, typography...etc
 */
object ThoughtsTheme {
    val lightPalette: Colors
        get() = LightColorPalette

    val darkPalette: Colors
        get() = DarkColorPalette
}

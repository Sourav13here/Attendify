package com.example.attendify.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = TextOnPrimary,
    secondary = SecondaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    onBackground = CharcoalBlue,
    onSurface = TextPrimary,
    error = ErrorColor
)

@Composable
fun AttendifyTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}

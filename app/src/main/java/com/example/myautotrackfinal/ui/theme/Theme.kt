package com.example.myautotrackfinal.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect // Asegúrate de que esta importación esté
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb // Asegúrate de que esta importación esté
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView // Asegúrate de que esta importación esté
import androidx.core.view.WindowCompat // Asegúrate de que esta importación esté

// Usa los nuevos nombres de colores definidos en Color.kt
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = Color(0xFF121212), // Fondo oscuro para modo oscuro
    surface = Color(0xFF121212),    // Superficie oscura
    onPrimary = White,
    onSecondary = Black,
    onTertiary = White,
    onBackground = White,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    tertiary = LightTertiary,
    background = Color(0xFFF0F2F5), // Fondo claro para modo claro
    surface = Color(0xFFFFFFFF),    // Superficie clara
    onPrimary = White,
    onSecondary = White,
    onTertiary = Black,
    onBackground = Black,
    onSurface = Black
)

@Composable
fun MyAutoTrackFinalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            // Si quieres que los iconos de la barra de estado sean claros en temas oscuros,
            // y oscuros en temas claros (para el contraste)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate de que Typography esté definido en Typography.kt
        content = content
    )
}
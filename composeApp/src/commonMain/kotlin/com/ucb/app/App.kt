package com.ucb.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.ucb.app.core.preferences.AppTheme
import com.ucb.app.core.preferences.UserPreferences
import com.ucb.app.navigation.AppNavHost

@Composable
fun App(
    destination: String? = null,
    onShowLocalNotification: () -> Unit = {},
    onRunWorker: () -> Unit = {},
    fcmToken: String = "",
    onOpenSystemSettings: () -> Unit = {}
) {
    val appTheme by UserPreferences.appTheme.collectAsState()
    val useDarkTheme = when (appTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }

    // Definición de colores UrbanBites Premium
    val urbanOrange = Color(0xFFFF5722)
    
    val darkColors = darkColorScheme(
        primary = urbanOrange,
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onBackground = Color.White,
        onSurface = Color.White,
        surfaceVariant = Color(0xFF2C2C2C),
        onSurfaceVariant = Color.LightGray
    )

    val lightColors = lightColorScheme(
        primary = urbanOrange,
        background = Color(0xFFF8F8F8),
        surface = Color.White,
        onBackground = Color(0xFF333333),
        onSurface = Color(0xFF333333),
        surfaceVariant = Color(0xFFEEEEEE),
        onSurfaceVariant = Color.Gray
    )

    MaterialTheme(
        colorScheme = if (useDarkTheme) darkColors else lightColors
    ) {
        // El Surface base asegura que el fondo sea del color correcto en toda la app
        Surface(color = MaterialTheme.colorScheme.background) {
            AppNavHost(
                destination = destination,
                onShowLocalNotification = onShowLocalNotification,
                onRunWorker = onRunWorker,
                fcmToken = fcmToken,
                onOpenSystemSettings = onOpenSystemSettings
            )
        }
    }
}

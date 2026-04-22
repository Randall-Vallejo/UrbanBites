package com.ucb.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.ucb.app.navigation.AppNavHost

@Composable
fun App(
    destination: String? = null,
    onShowLocalNotification: () -> Unit = {},
    onRunWorker: () -> Unit = {},
    fcmToken: String = ""
) {
    MaterialTheme {
        AppNavHost(
            destination = destination,
            onShowLocalNotification = onShowLocalNotification,
            onRunWorker = onRunWorker,
            fcmToken = fcmToken
        )
    }
}

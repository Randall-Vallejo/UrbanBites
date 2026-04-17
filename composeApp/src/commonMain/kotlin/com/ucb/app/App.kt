package com.ucb.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.ucb.app.presentation.navigation.AppNavHost

@Composable
fun App() {
    MaterialTheme {
        AppNavHost()
    }
}

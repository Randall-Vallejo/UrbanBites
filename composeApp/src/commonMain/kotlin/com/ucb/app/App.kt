package com.ucb.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.ucb.app.navigation.AppNavHost

@Composable
fun App(destination: String? = null) {
    MaterialTheme {
        AppNavHost(destination = destination)
    }
}

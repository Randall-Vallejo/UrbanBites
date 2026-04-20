package com.ucb.app.crypto.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ucb.app.crypto.presentation.composable.CryptoContent
import com.ucb.app.crypto.presentation.state.CryptoEffect
import com.ucb.app.crypto.presentation.state.CryptoEvent
import com.ucb.app.crypto.presentation.viewmodel.CryptoViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CryptoScreen(
    viewModel: CryptoViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(CryptoEvent.OnLoad)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CryptoEffect.ShowError -> {
                    //show error
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.cryptos.isNotEmpty() -> {
                CryptoContent(state.cryptos)
            }

            state.error != null -> {
                Text("Error: ${state.error}", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

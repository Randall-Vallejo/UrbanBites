package com.ucb.app.crypto.presentation.state

sealed interface CryptoEffect {
    data class ShowError(val message: String) : CryptoEffect
}
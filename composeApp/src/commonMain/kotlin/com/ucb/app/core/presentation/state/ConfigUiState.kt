package com.ucb.app.core.presentation.state

data class ConfigUiState(
    val configMessage: String = "Cargando...",
    val isLoading: Boolean = false
)

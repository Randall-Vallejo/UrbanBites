package com.ucb.app.login.presentation.state

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

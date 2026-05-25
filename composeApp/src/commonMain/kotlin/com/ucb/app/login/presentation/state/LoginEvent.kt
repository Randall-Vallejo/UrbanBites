package com.ucb.app.login.presentation.state

sealed interface LoginEvent {
    object OnLoginClicked: LoginEvent
    object OnContinueAsGuestClicked: LoginEvent
    object OnTogglePasswordVisibilityClicked: LoginEvent
    data class OnEmailChanged(val value: String): LoginEvent
    data class OnPasswordChanged(val value: String): LoginEvent
}
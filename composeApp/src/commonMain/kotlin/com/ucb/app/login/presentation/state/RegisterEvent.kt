package com.ucb.app.login.presentation.state

sealed interface RegisterEvent {
    data class OnNameChanged(val value: String): RegisterEvent
    data class OnEmailChanged(val value: String): RegisterEvent
    data class OnPasswordChanged(val value: String): RegisterEvent
    data class OnConfirmPasswordChanged(val value: String): RegisterEvent
    object OnTogglePasswordVisibilityClicked: RegisterEvent
    object OnRegisterClicked: RegisterEvent
}

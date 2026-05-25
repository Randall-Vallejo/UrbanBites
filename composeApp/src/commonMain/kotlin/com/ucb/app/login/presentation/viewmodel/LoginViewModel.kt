package com.ucb.app.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.login.domain.usecase.DoLoginUseCase
import com.ucb.app.login.presentation.state.LoginEffect
import com.ucb.app.login.presentation.state.LoginEvent
import com.ucb.app.login.presentation.state.LoginUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    val loginUseCase: DoLoginUseCase
): ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            LoginEvent.OnLoginClicked -> validateAndLogin()
            LoginEvent.OnContinueAsGuestClicked -> {
                emit(LoginEffect.NavigateToHome)
            }
            LoginEvent.OnTogglePasswordVisibilityClicked -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is LoginEvent.OnEmailChanged -> {
                _state.update { it.copy(email = event.value, error = null) }
            }
            is LoginEvent.OnPasswordChanged -> {
                _state.update { it.copy(password = event.value, error = null) }
            }
        }
    }

    private fun validateAndLogin() {
        val email = _state.value.email.trim()
        val password = _state.value.password.trim()

        if (email.isBlank()) {
            _state.update { it.copy(error = "EMPTY_EMAIL") }
            return
        }

        if (password.isBlank()) {
            _state.update { it.copy(error = "EMPTY_PASSWORD") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            delay(1000)

            // Credenciales oficiales de UrbanBites - Ahora más flexibles
            if ((email == "admin" || email == "admin@urbanbites.com") && password == "123456") {
                _state.update { it.copy(isLoading = false, isLoggedIn = true) }
                emit(LoginEffect.NavigateToHome)
            } else {
                _state.update { it.copy(isLoading = false, error = "INVALID_CREDENTIALS") }
            }
        }
    }

    private fun emit(effect: LoginEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}

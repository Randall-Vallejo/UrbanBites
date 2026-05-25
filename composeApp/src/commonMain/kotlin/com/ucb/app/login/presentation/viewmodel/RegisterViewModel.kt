package com.ucb.app.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.login.presentation.state.LoginEffect
import com.ucb.app.login.presentation.state.RegisterEvent
import com.ucb.app.login.presentation.state.RegisterUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnNameChanged -> _state.update { it.copy(name = event.value, error = null) }
            is RegisterEvent.OnEmailChanged -> _state.update { it.copy(email = event.value, error = null) }
            is RegisterEvent.OnPasswordChanged -> _state.update { it.copy(password = event.value, error = null) }
            is RegisterEvent.OnConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = event.value, error = null) }
            RegisterEvent.OnTogglePasswordVisibilityClicked -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            RegisterEvent.OnRegisterClicked -> register()
        }
    }

    private fun register() {
        val s = _state.value
        if (s.name.isBlank() || s.email.isBlank() || s.password.isBlank()) {
            _state.update { it.copy(error = "Por favor completa todos los campos") }
            return
        }
        if (s.password != s.confirmPassword) {
            _state.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1500) // Simulación
            // Aquí irá la lógica de Firebase Auth
            _state.update { it.copy(isLoading = false) }
            _effect.emit(LoginEffect.NavigateToHome)
        }
    }
}

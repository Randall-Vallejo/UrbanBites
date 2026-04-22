package com.ucb.app.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.login.domain.model.LoginModel
import com.ucb.app.login.domain.usecase.DoLoginUseCase
import com.ucb.app.login.presentation.state.LoginEffect
import com.ucb.app.login.presentation.state.LoginEvent
import com.ucb.app.login.presentation.state.LoginUiState
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
            LoginEvent.OnClick -> sendLogin()
            is LoginEvent.OnEmailChanged -> {
                _state.update { it.copy(email = event.value) }
            }
            is LoginEvent.OnPasswordChanged -> {
                _state.update { it.copy(password = event.value) }
            }
        }
    }

    private fun sendLogin() {
        val emailValue = _state.value.email
        val passwordValue = _state.value.password

        viewModelScope.launch {
            if (emailValue == "admin" && passwordValue == "123") {
                _state.update { it.copy(isLoggedIn = true) }
            } else {
                _state.update { it.copy(error = "Usuario o clave incorrectos") }
            }
        }
    }

    private fun emit(effect: LoginEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}

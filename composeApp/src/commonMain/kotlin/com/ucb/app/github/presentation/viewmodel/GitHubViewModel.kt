package com.ucb.app.github.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.github.data.dto.GitHubErrorDto
import com.ucb.app.github.domain.usecase.GetGitHubUserUseCase
import com.ucb.app.github.presentation.state.GitHubState
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GitHubViewModel(
    private val getGitHubUserUseCase: GetGitHubUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GitHubState())
    val state: StateFlow<GitHubState> = _state.asStateFlow()

    fun onUsernameChange(value: String) {
        _state.value = _state.value.copy(
            username = value,
            error = null
        )
    }

    fun searchUser() {
        val username = _state.value.username.trim()

        if (username.isEmpty()) {
            _state.value = _state.value.copy(
                error = "Ingresa un username",
                user = null
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null,
                user = null
            )

            try {
                val user = getGitHubUserUseCase(username)
                _state.value = _state.value.copy(
                    isLoading = false,
                    user = user,
                    error = null
                )
            } catch (e: ClientRequestException) {
                val errorMessage = try {
                    val errorResponse: GitHubErrorDto = e.response.body()
                    errorResponse.message
                } catch (ex: Exception) {
                    "Error desconocido"
                }

                val finalMessage = if (errorMessage == "Not Found") {
                    "Usuario no encontrado ($errorMessage)"
                } else {
                    "Error: $errorMessage"
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    user = null,
                    error = finalMessage
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    user = null,
                    error = "Ocurrió un error: ${e.message}"
                )
            }
        }
    }
}

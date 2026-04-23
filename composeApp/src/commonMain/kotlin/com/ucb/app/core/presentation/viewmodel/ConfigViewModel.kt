package com.ucb.app.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.core.domain.usecase.GetAppConfigUseCase
import com.ucb.app.core.presentation.state.ConfigUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfigViewModel(
    private val getAppConfigUseCase: GetAppConfigUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ConfigUiState())
    val state = _state.asStateFlow()

    init {
        loadConfig()
    }

    fun loadConfig() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val message = getAppConfigUseCase("app_config_message")
            _state.update { it.copy(configMessage = message, isLoading = false) }
        }
    }
}

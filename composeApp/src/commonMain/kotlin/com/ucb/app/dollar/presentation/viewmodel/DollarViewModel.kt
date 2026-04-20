package com.ucb.app.dollar.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.dollar.domain.model.DollarModel
import com.ucb.app.dollar.domain.usecase.CreateDollarUseCase
import com.ucb.app.dollar.domain.usecase.GetDollarListUsecase
import com.ucb.app.dollar.presentation.state.DollarUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DollarViewModel(
    val useCase: GetDollarListUsecase,
    val createUseCase: CreateDollarUseCase
): ViewModel() {

    private val _state = MutableStateFlow(DollarUIState())
    val state = _state.asStateFlow()

    init {
        createDataTest()
        loadData()
    }

    fun createDataTest() = viewModelScope.launch {
        createUseCase.invoke(DollarModel(dollarOfficial = "6.96", dollarParallel = "9.96"))
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    list = useCase.invoke(),
                    isLoading = false
                )
            }
        }
    }
}

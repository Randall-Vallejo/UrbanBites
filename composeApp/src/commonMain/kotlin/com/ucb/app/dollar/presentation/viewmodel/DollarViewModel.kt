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
import kotlin.random.Random

class DollarViewModel(
    val useCase: GetDollarListUsecase,
    val createUseCase: CreateDollarUseCase
): ViewModel() {

    private val _state = MutableStateFlow(DollarUIState())
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun createDataTest() = viewModelScope.launch {
        // Simulamos un cambio real con valores aleatorios entre 9.00 y 11.00
        val randomParallel = (900 + Random.nextInt(200)) / 100.0
        
        createUseCase.invoke(
            DollarModel(
                dollarOfficial = "6.96", 
                dollarParallel = randomParallel.toString()
            )
        )
        loadData() // Recargamos la lista para ver el nuevo valor
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val list = useCase.invoke()
            _state.update {
                it.copy(
                    list = list.reversed(), // Mostramos el más reciente arriba
                    isLoading = false
                )
            }
        }
    }
}

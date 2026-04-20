package com.ucb.app.increment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class IncrementViewModel: ViewModel() {

    //1   variable mutable
    private val _counterState = MutableStateFlow(0)

    //2  variable observable
    val counterState = _counterState.asStateFlow()

    //3 evento desencadenador
    fun incrementData() {
        _counterState.value = _counterState.value + 1
    }


}
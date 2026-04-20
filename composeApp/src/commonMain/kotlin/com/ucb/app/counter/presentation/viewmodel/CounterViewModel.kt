package com.ucb.app.counter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CounterViewModel : ViewModel() {

    //1  la variable mutable
    private val _stateString: MutableStateFlow<String> = MutableStateFlow("0")

    //2 la variable observable
    val stateString = _stateString.asStateFlow()


    //3 evento desencadenador
    fun increment() {
        _stateString.value = ((_stateString.value?.toIntOrNull() ?:0) + 1).toString()

    }

}
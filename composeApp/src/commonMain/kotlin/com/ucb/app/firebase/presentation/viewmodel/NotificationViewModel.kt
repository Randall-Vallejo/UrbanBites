package com.ucb.app.firebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.firebase.getToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {
    private val _token = MutableStateFlow("")
    val token = _token.asStateFlow()

    init {
        fetchToken()
    }

    fun fetchToken() {
        viewModelScope.launch {
            try {
                val fcmToken = getToken()
                _token.update { fcmToken }
            } catch (e: Exception) {
                _token.update { "Error: ${e.message}" }
            }
        }
    }
}

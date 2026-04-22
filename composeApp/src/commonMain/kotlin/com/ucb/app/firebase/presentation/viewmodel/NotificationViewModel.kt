package com.ucb.app.firebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.firebase.data.datasource.RemoteConfigManager
import com.ucb.app.firebase.getToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val remoteConfigManager: RemoteConfigManager,
    private val firebaseManager: FirebaseManager
) : ViewModel() {
    private val _token = MutableStateFlow("")
    val token = _token.asStateFlow()

    // Iniciamos intentando leer el último valor conocido de Remote Config
    private val _cloudMessage = MutableStateFlow(remoteConfigManager.getString("welcome_message").ifEmpty { "Cargando..." })
    val cloudMessage = _cloudMessage.asStateFlow()

    private val _messageToSend = MutableStateFlow("")
    val messageToSend = _messageToSend.asStateFlow()

    init {
        fetchToken()
        // Ejecutamos la actualización automática al abrir la app
        autoUpdateRemoteConfig()
    }

    private fun autoUpdateRemoteConfig() {
        viewModelScope.launch {
            // Intentamos traer lo nuevo de la nube
            if (remoteConfigManager.fetchAndActivate()) {
                val newMessage = remoteConfigManager.getString("welcome_message")
                if (newMessage.isNotEmpty()) {
                    _cloudMessage.update { newMessage }
                }
            }
        }
    }

    fun onMessageChange(newMessage: String) {
        _messageToSend.update { newMessage }
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

    fun fetchRemoteConfig() {
        autoUpdateRemoteConfig()
    }

    fun sendCustomMessage() {
        val message = _messageToSend.value
        if (message.isNotEmpty()) {
            viewModelScope.launch {
                firebaseManager.saveData("test/user_message", message)
                _messageToSend.update { "" }
            }
        }
    }
}

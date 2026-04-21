package com.ucb.app.firebase.data.datasource

// Definimos que existirá un RemoteConfigManager en cada plataforma
expect class RemoteConfigManager() {
    // Función que nos traerá un texto desde la nube
    fun getWelcomeMessage(): String
}
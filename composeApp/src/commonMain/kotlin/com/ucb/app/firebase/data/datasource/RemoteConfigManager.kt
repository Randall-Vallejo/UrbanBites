package com.ucb.app.firebase.data.datasource

// Definimos el contrato que las plataformas (Android/iOS) deben implementar
expect class RemoteConfigManager() {

    // Función genérica de Randall para obtener cualquier valor
    fun getString(key: String): String

    // Función para asegurar la descarga de datos
    suspend fun fetchAndActivate(): Boolean

    // Función específica de Huayna para el mensaje de bienvenida
    fun getWelcomeMessage(): String
}
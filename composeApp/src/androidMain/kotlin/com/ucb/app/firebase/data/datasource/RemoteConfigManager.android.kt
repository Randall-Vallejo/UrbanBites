package com.ucb.app.firebase.data.datasource

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

actual class RemoteConfigManager actual constructor() {
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    init {
        // Configuramos el tiempo de recarga (0 segundos para desarrollo/pruebas)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Valores por defecto (Lo que trajo Huayna)
        remoteConfig.setDefaultsAsync(mapOf(
            "welcome_message" to "Bienvenido a UrbanBites (Local)"
        ))

        // Intentar traer datos de la nube inmediatamente
        remoteConfig.fetchAndActivate()
    }

    // Función genérica de Randall (Súper útil para cualquier llave futura)
    actual suspend fun fetchAndActivate(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            false
        }
    }

    // Función genérica de Randall
    actual fun getString(key: String): String {
        val value = remoteConfig.getString(key)
        return if (value.isEmpty()) "Cargando..." else value
    }

    // Función específica de Huayna (Para no romper sus pantallas)
    actual fun getWelcomeMessage(): String {
        return remoteConfig.getString("welcome_message")
    }
}
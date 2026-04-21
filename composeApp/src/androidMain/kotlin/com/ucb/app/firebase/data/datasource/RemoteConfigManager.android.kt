package com.ucb.app.firebase.data.datasource

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

actual class RemoteConfigManager actual constructor() {
    private val remoteConfig = Firebase.remoteConfig

    init {
        // Configuramos el tiempo de recarga (0 segundos para desarrollo/pruebas)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Valor por defecto en caso de que no haya internet
        remoteConfig.setDefaultsAsync(mapOf(
            "welcome_message" to "Bienvenido a UrbanBites (Local)"
        ))

        // Traer el valor más reciente de la nube
        remoteConfig.fetchAndActivate()
    }

    actual fun getWelcomeMessage(): String {
        return remoteConfig.getString("welcome_message")
    }
}
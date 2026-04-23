package com.ucb.app.firebase.data.datasource

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.ucb.app.core.data.db.ConfigEntity
import com.ucb.app.core.data.db.dao.ConfigDao
import kotlinx.coroutines.tasks.await

actual class RemoteConfigManager actual constructor(private val configDao: ConfigDao) {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0) // 0 para ver cambios inmediatos en el examen
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    actual suspend fun fetchAndActivate(): Boolean {
        return try {
            val updated = remoteConfig.fetchAndActivate().await()
            if (updated) {
                // Sincronización inicial con caché local: Guardamos en Room lo que bajamos
                val welcomeMsg = remoteConfig.getString("welcome_message")
                configDao.saveConfig(ConfigEntity("welcome_message", welcomeMsg))
            }
            updated
        } catch (e: Exception) {
            false
        }
    }

    actual fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    // Si no hay internet, usamos la última versión guardada en Room
    actual suspend fun getCachedValue(key: String): String {
        return configDao.getConfig(key)?.value ?: "Sin caché"
    }
}

package com.ucb.app.core.data.repository

import com.ucb.app.core.data.db.dao.AppConfigDao
import com.ucb.app.core.data.db.entity.AppConfigEntity
import com.ucb.app.core.domain.repository.ConfigRepository
import com.ucb.app.firebase.data.datasource.RemoteConfigManager

class ConfigRepositoryImpl(
    private val remoteConfigManager: RemoteConfigManager,
    private val appConfigDao: AppConfigDao
) : ConfigRepository {

    override suspend fun getAppConfig(key: String): String {
        return try {
            // 1. Intentar descargar de Firebase
            val success = remoteConfigManager.fetchAndActivate()
            if (success) {
                val remoteValue = remoteConfigManager.getString(key)
                if (remoteValue.isNotEmpty()) {
                    // 2. Guardar en Room para caché
                    appConfigDao.saveConfig(
                        AppConfigEntity(
                            key = key,
                            value = remoteValue,
                            lastUpdated = 0L // Simplificado
                        )
                    )
                    return remoteValue
                }
            }
            // 3. Si falla la descarga, usar caché local
            getConfigFromCache(key)
        } catch (e: Exception) {
            // 4. En caso de error, usar caché local
            getConfigFromCache(key)
        }
    }

    private suspend fun getConfigFromCache(key: String): String {
        return appConfigDao.getConfig(key)?.value ?: "Valor por defecto (Sin conexión)"
    }
}

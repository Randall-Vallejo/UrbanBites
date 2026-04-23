package com.ucb.app.core.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ucb.app.core.data.db.AppDatabase
import com.ucb.app.core.data.db.entity.ConfigEntity
import com.ucb.app.core.data.notification.LocalNotificationHelper
import com.ucb.app.firebase.data.datasource.RemoteConfigManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val remoteConfigManager: RemoteConfigManager by inject()
    private val database: AppDatabase by inject()

    override suspend fun doWork(): Result {
        Log.i("WORKER_DEBUG", "¡El Worker ha despertado!")
        
        return try {
            // 1. Forzar descarga de Remote Config
            val success = remoteConfigManager.fetchAndActivate()
            
            if (success) {
                // 2. Usar la clave de Randall
                val configKey = "welcome_message"
                val remoteValue = remoteConfigManager.getString(configKey)
                
                // 3. Consultar Room
                val configDao = database.configDao()
                val cachedConfig = configDao.getConfigByKey(configKey)
                val localValue = cachedConfig?.value ?: ""

                Log.i("WORKER_DEBUG", "Remoto: $remoteValue | Local en Room: $localValue")

                // 4. Si el valor de la nube es diferente al de la base de datos
                if (remoteValue != localValue && remoteValue != "Cargando...") {
                    
                    // 5. Guardar el nuevo valor en Room
                    configDao.saveConfig(
                        ConfigEntity(key = configKey, value = remoteValue, updatedAt = System.currentTimeMillis())
                    )

                    // 6. LANZAR NOTIFICACIÓN REAL
                    val notificationHelper = LocalNotificationHelper(applicationContext)
                    notificationHelper.showNotification(
                        "Configuración Actualizada",
                        "El mensaje cambió a: $remoteValue"
                    )
                    Log.i("WORKER_DEBUG", "¡Notificación enviada!")
                } else {
                    Log.i("WORKER_DEBUG", "No hay cambios, no se envía notificación.")
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("WORKER_DEBUG", "Error: ${e.message}")
            Result.retry()
        }
    }
}

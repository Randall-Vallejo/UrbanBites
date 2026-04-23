package com.ucb.app.core.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ucb.app.core.domain.usecase.GetAppConfigUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncConfigWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val getAppConfigUseCase: GetAppConfigUseCase by inject()

    override suspend fun doWork(): Result {
        Log.i("SYNC_WORKER", "Iniciando sincronización de configuración inicial en segundo plano...")
        
        return try {
            // El caso de uso ya maneja la lógica de: Descargar de Firebase -> Guardar en Room
            val config = getAppConfigUseCase("app_config_message")
            Log.i("SYNC_WORKER", "✅ Configuración sincronizada con éxito: $config")
            Result.success()
        } catch (e: Exception) {
            Log.e("SYNC_WORKER", "❌ Error en la sincronización inicial: ${e.message}")
            Result.retry()
        }
    }
}

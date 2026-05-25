package com.ucb.app.core.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent

class MyWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    override suspend fun doWork(): Result {
        // Log básico para verificar que el worker funciona sin dependencias inexistentes
        Log.i("WORKER_INFO", "¡El Worker de UrbanBites ha despertado!")
        
        return try {
            // Aquí puedes añadir lógica de sincronización real en el futuro
            Log.i("WORKER_INFO", "✅ Tarea de background completada exitosamente.")
            Result.success()
        } catch (e: Exception) {
            Log.e("WORKER_INFO", "❌ Fallo en background: ${e.message}")
            Result.retry()
        }
    }
}

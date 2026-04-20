package com.ucb.app.core.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ucb.app.github.domain.usecase.GetGitHubUserUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    // Usamos lazy inject para evitar problemas si Koin no ha terminado de cargar
    private val getGitHubUserUseCase: GetGitHubUserUseCase by inject()

    override suspend fun doWork(): Result {
        // Log básico que sale sí o sí
        Log.i("WORKER_INFO", "¡El Worker ha despertado!")
        
        return try {
            val user = getGitHubUserUseCase("jskeet")
            Log.i("WORKER_INFO", "✅ Datos de background: ${user.name}")
            Result.success()
        } catch (e: Exception) {
            Log.e("WORKER_INFO", "❌ Fallo en background: ${e.message}")
            Result.retry()
        }
    }
}

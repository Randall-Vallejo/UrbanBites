package com.ucb.app.core.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ucb.app.core.domain.repository.EventRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncEventsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val eventRepository: EventRepository by inject()

    override suspend fun doWork(): Result {
        Log.i("SYNC_EVENTS", "Sincronizando eventos con Firebase en segundo plano...")
        
        return try {
            eventRepository.syncEventsWithFirebase()
            Log.i("SYNC_EVENTS", "✅ Sincronización de eventos completada.")
            Result.success()
        } catch (e: Exception) {
            Log.e("SYNC_EVENTS", "❌ Error al sincronizar eventos: ${e.message}")
            Result.retry()
        }
    }
}

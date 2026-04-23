package com.ucb.app.core.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ucb.app.core.data.db.EventEntity
import com.ucb.app.core.data.db.dao.EventDao
import com.ucb.app.firebase.data.datasource.FirebaseManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EventWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val eventDao: EventDao by inject()
    private val firebaseManager: FirebaseManager by inject()

    override suspend fun doWork(): Result {
        Log.d("EventWorker", "🔥 Registrando evento de apertura en background...")
        
        val timestamp = System.currentTimeMillis()
        val eventType = "App Abierta (Examen)"
        
        return try {
            // 1. Guardar en Room (Persistencia Local)
            eventDao.insertEvent(EventEntity(timestamp = timestamp, type = eventType))
            
            // 2. Replicar en Realtime Database (Si hay conexión)
            firebaseManager.saveData("eventsRandallVallejo/$timestamp", eventType)
            
            Log.d("EventWorker", "✅ Evento registrado en Room y Firebase")
            Result.success()
        } catch (e: Exception) {
            Log.e("EventWorker", "❌ Error registrando evento: ${e.message}")
            Result.retry()
        }
    }
}

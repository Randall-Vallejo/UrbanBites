package com.ucb.app.core.data.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class MyScheduler(private val context: Context) {

    fun start() {
        val request = PeriodicWorkRequestBuilder<MyWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "urban_bites_periodic_work",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    // Registra el evento de apertura (Punto 2 del examen)
    fun logAppOpenEvent() {
        val request = OneTimeWorkRequestBuilder<EventWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }

    // Método para prueba rápida del worker genérico
    fun runNow() {
        val request = OneTimeWorkRequestBuilder<MyWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}

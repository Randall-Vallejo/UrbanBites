package com.ucb.app.core.data.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class MyScheduler(private val context: Context) {

    fun start() {
        // Tarea periódica solicitada: CheckNearbyTrucksWorker
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<CheckNearbyTrucksWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(constraints)
        .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "urban_bites_nearby_check",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    // Método para ejecución inmediata de prueba
    fun runNow() {
        val request = OneTimeWorkRequestBuilder<CheckNearbyTrucksWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}

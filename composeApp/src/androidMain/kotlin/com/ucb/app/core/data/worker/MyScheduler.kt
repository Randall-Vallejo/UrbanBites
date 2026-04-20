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

    // Método para prueba rápida (ejecuta una vez inmediatamente)
    fun runNow() {
        val request = OneTimeWorkRequestBuilder<MyWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}

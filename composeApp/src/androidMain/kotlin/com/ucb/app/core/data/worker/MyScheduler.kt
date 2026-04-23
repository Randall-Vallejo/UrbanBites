package com.ucb.app.core.data.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class MyScheduler(private val context: Context) {

    fun start() {
        // Tarea periódica existente (GitHub demo)
        val periodicRequest = PeriodicWorkRequestBuilder<MyWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "urban_bites_periodic_work",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )

        // EJERCICIO 1: Sincronización inicial única
        val syncConfigRequest = OneTimeWorkRequestBuilder<SyncConfigWorker>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork("initial_config_sync", ExistingWorkPolicy.REPLACE, syncConfigRequest)

        // EJERCICIO 4: Sincronización periódica de eventos
        val syncEventsRequest = PeriodicWorkRequestBuilder<SyncEventsWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "sync_events_periodic",
            ExistingPeriodicWorkPolicy.KEEP,
            syncEventsRequest
        )
    }

    // Método para sincronizar eventos inmediatamente (útil para la demo)
    fun syncEventsNow() {
        val request = OneTimeWorkRequestBuilder<SyncEventsWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }

    fun runNow() {
        val request = OneTimeWorkRequestBuilder<MyWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}

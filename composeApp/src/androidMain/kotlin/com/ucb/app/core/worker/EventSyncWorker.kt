package com.ucb.app.core.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ucb.app.core.domain.repository.SyncRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EventSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val syncRepository: SyncRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            syncRepository.syncEventsToFirebase()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

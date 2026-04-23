package com.ucb.app

import android.app.Application
import android.os.Bundle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ucb.app.core.domain.repository.SyncRepository
import com.ucb.app.core.worker.InitialSyncWorker
import com.ucb.app.core.worker.EventSyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import com.ucb.app.di.initKoin

class AndroidApp : Application() {

    private val syncRepository: SyncRepository by inject()
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var activityCount = 0

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AndroidApp)
        }

        setupLifecycleListener()
        scheduleInitialSync()
    }

    private fun setupLifecycleListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: android.app.Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: android.app.Activity) {
                if (activityCount == 0) {
                    // App en primer plano
                    logAppEvent("OPEN")
                }
                activityCount++
            }
            override fun onActivityResumed(activity: android.app.Activity) {}
            override fun onActivityPaused(activity: android.app.Activity) {}
            override fun onActivityStopped(activity: android.app.Activity) {
                activityCount--
                if (activityCount == 0) {
                    // App en segundo plano
                    appScope.launch {
                        syncRepository.logEvent("CLOSE", System.currentTimeMillis())
                        scheduleEventSync()
                    }
                }
            }
            override fun onActivitySaveInstanceState(activity: android.app.Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: android.app.Activity) {}
        })
    }

    private fun logAppEvent(type: String) {
        appScope.launch {
            syncRepository.logEvent(type, System.currentTimeMillis())
            if (type == "OPEN") {
                // Sincronizar también al abrir para subir lo que quedó pendiente al cerrar
                scheduleEventSync()
            }
        }
    }

    private fun scheduleInitialSync() {
        val request = OneTimeWorkRequestBuilder<InitialSyncWorker>().build()
        WorkManager.getInstance(this).enqueue(request)
    }

    private fun scheduleEventSync() {
        val request = OneTimeWorkRequestBuilder<EventSyncWorker>().build()
        WorkManager.getInstance(this).enqueue(request)
    }
}

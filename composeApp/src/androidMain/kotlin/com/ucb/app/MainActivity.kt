package com.ucb.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.ucb.app.core.data.notification.LocalNotificationHelper
import com.ucb.app.core.data.worker.MyScheduler
import com.ucb.app.core.domain.repository.EventRepository
import org.koin.android.ext.android.inject
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private var destination by mutableStateOf<String?>(null)
    private var fcmToken by mutableStateOf("")
    
    private val eventRepository: EventRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val scheduler = MyScheduler(this)
        val notificationHelper = LocalNotificationHelper(this)

        // REGISTRO DE EVENTO: ABRIR
        lifecycleScope.launch {
            eventRepository.recordEvent("OPEN")
        }

        scheduler.start()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fcmToken = task.result
                Log.d("FCM_TOKEN", "Token actual: $fcmToken")
            }
        }

        destination = intent.getStringExtra("destination")

        setContent {
            App(
                destination = destination,
                onShowLocalNotification = {
                    notificationHelper.showNotification(
                        "Notificación Interna", 
                        "Esta es una prueba local sin Firebase."
                    )
                },
                onRunWorker = {
                    scheduler.runNow()
                    scheduler.syncEventsNow()
                },
                fcmToken = fcmToken
            )
        }
    }

    override fun onStop() {
        super.onStop()
        // REGISTRO DE EVENTO: CERRAR
        // Usamos lifecycleScope para que la corrutina tenga tiempo de ejecutarse antes de que la app muera
        lifecycleScope.launch {
            eventRepository.recordEvent("CLOSE")
            Log.d("EVENT_LOG", "Evento CLOSE registrado en Room")
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        destination = intent.getStringExtra("destination")
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

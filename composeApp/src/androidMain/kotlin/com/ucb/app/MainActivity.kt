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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private var destination by mutableStateOf<String?>(null)
    private var fcmToken by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val scheduler = MyScheduler(this)
        val notificationHelper = LocalNotificationHelper(this)

        // Lanzamos el registro de apertura en segundo plano (Punto 2 examen)
        lifecycleScope.launch {
            delay(2000) // Esperamos a que Firebase se inicialice bien
            scheduler.logAppOpenEvent()
        }

        // Obtener el Token FCM
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
                },
                fcmToken = fcmToken
            )
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

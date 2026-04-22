package com.ucb.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.messaging.FirebaseMessaging
import com.ucb.app.core.data.notification.LocalNotificationHelper
import com.ucb.app.core.data.worker.MyScheduler

class MainActivity : ComponentActivity() {
    
    private var destination by mutableStateOf<String?>(null)
    private var fcmToken by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val scheduler = MyScheduler(this)
        val notificationHelper = LocalNotificationHelper(this)

        // Iniciamos el Scheduler de tareas en segundo plano
        scheduler.start()

        // Obtener el Token FCM para mostrarlo en la demo
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

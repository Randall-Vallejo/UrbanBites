package com.ucb.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.ucb.app.core.data.notification.LocalNotificationHelper
import com.ucb.app.core.data.worker.MyScheduler

class MainActivity : ComponentActivity() {
    
    private var destination by mutableStateOf<String?>(null)
    private var fcmToken by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // 1. Crear canal de notificaciones (Esencial para Android 8.0+)
        createNotificationChannel()
        
        // 2. Solicitar permisos (Esencial para Android 13+)
        checkNotificationPermission()

        val scheduler = MyScheduler(this)
        val notificationHelper = LocalNotificationHelper(this)

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
                        "¡Has guardado un favorito!"
                    )
                },
                onRunWorker = {
                    scheduler.runNow()
                },
                fcmToken = fcmToken
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "urban_bites_channel_v5"
            val name = "Urban Bites VIP"
            val descriptionText = "Canal prioritario para notificaciones push"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != 
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
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

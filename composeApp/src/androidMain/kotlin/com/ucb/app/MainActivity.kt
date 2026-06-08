package com.ucb.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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

        createNotificationChannel()
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
                    notificationHelper.showNotification("UrbanBites", "¡Prueba local!")
                },
                onRunWorker = { scheduler.runNow() },
                fcmToken = fcmToken,
                onOpenSystemSettings = {
                    // Requerimiento 4: Acceso directo a Ajustes del Sistema
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                    startActivity(intent)
                }
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "urban_bites_channel_v5", 
                "Urban Bites VIP", 
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }
}

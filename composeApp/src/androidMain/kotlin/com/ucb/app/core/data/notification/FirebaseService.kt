package com.ucb.app.core.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ucb.app.MainActivity

class FirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_DEBUG", "NUEVO TOKEN GENERADO: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        // LOG CRUCIAL: Si ves esto en Logcat, el problema es la UI. Si NO lo ves, el problema es Firebase/Token.
        Log.d("FCM_DEBUG", "¡¡MENSAJE RECIBIDO!!")
        Log.d("FCM_DEBUG", "Payload Data: ${message.data}")
        Log.d("FCM_DEBUG", "Payload Notification: ${message.notification?.body}")

        val title = message.notification?.title ?: message.data["title"] ?: "UrbanBites"
        val body = message.notification?.body ?: message.data["body"] ?: "¡Tienes una actualización!"
        
        sendNotification(title, body)
    }

    private fun sendNotification(title: String, body: String) {
        val channelId = "urban_bites_channel_v5"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Aseguramos que el canal exista (doble verificación)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notificaciones VIP", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Para que salga el banner arriba
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        Log.d("FCM_DEBUG", "Notificación disparada al sistema.")
    }
}

package com.ucb.app.core.notification

class IosNotificationProvider : NotificationProvider {
    override fun showLocalNotification(title: String, message: String) {
        // Implementación pendiente para iOS usando UNUserNotificationCenter
        println("iOS Notification: $title - $message")
    }
}

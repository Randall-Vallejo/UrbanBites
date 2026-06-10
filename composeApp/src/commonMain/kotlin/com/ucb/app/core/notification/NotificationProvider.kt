package com.ucb.app.core.notification

interface NotificationProvider {
    fun showLocalNotification(title: String, message: String)
}

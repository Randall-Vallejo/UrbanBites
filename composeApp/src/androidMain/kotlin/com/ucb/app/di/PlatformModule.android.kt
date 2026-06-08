package com.ucb.app.di

import com.ucb.app.core.notification.AndroidNotificationProvider
import com.ucb.app.core.notification.NotificationProvider
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<NotificationProvider> { AndroidNotificationProvider(get()) }
}

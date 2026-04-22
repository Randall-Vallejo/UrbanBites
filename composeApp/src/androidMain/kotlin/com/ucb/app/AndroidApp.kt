package com.ucb.app

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import com.ucb.app.di.initKoin

class AndroidApp : Application() {
    
    companion object {
        private var instance: AndroidApp? = null
        fun getContext(): Context = instance!!.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        initKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AndroidApp)
        }
    }
}

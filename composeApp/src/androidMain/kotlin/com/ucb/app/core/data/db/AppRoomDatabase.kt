package com.ucb.app.core.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ucb.app.AndroidApp

actual fun getDatabaseBuilder(ctx: Any?): RoomDatabase.Builder<AppDatabase> {
    // Si Koin no nos pasa el contexto, lo tomamos del singleton seguro de la App
    val context = (ctx as? Context) ?: AndroidApp.getContext()
    val dbFile = context.getDatabasePath("dollar.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}

package com.ucb.app.core.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.context.GlobalContext

actual fun getDatabaseBuilder(ctx: Any?): RoomDatabase.Builder<AppDatabase> {
    // Recuperamos el Context de forma segura
    val context = (ctx as? Context) ?: GlobalContext.get().get<Context>()

    // Nombre único para forzar una base de datos limpia y estable
    val dbFile = context.getDatabasePath("urbanbites_production_v1.db") 
    
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    ).fallbackToDestructiveMigration(true) // Limpia la DB si hay cambios de esquema
}

package com.ucb.app.core.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.context.GlobalContext

actual fun getDatabaseBuilder(ctx: Any?): RoomDatabase.Builder<AppDatabase> {
    // Magia: Si ctx es nulo, recuperamos el Context global que guardó Koin
    val context = (ctx as? Context) ?: GlobalContext.get().get<Context>()

    val dbFile = context.getDatabasePath("dollar.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    ).fallbackToDestructiveMigration() // Evita crashes al cambiar la versión a 2
}
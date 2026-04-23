package com.ucb.app.core.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(ctx: Any?): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/dollar.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        factory = { AppDatabaseConstructor.initialize() }
    )
}

package com.ucb.app.core.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.ucb.app.core.data.db.dao.DemoDao
import com.ucb.app.core.data.db.entity.DemoEntity

@Database(
    entities = [
        DemoEntity::class
    ],
    version = 4
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun demoDao(): DemoDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>

package com.ucb.app.core.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.ucb.app.core.data.db.dao.DemoDao
import com.ucb.app.core.data.db.entity.DemoEntity
import com.ucb.app.home.data.db.dao.FavoriteDao
import com.ucb.app.home.data.db.entity.FavoriteTruckEntity

@Database(
    entities = [
        DemoEntity::class,
        FavoriteTruckEntity::class
    ],
    version = 5
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun demoDao(): DemoDao
    abstract fun favoriteDao(): FavoriteDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>

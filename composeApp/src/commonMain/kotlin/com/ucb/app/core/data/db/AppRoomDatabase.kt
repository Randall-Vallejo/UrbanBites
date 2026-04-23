package com.ucb.app.core.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.ucb.app.core.data.db.dao.*
import com.ucb.app.core.data.db.entity.*
import com.ucb.app.dollar.data.dao.DollarDao
import com.ucb.app.dollar.data.entity.DollarEntity

@Database(
    entities = [
        DollarEntity::class,
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        NotificationEntity::class,
        DemoEntity::class,
        ConfigEntity::class,
        EventEntity::class,
        RemoteConfigHistoryEntity::class
    ],
    version = 5
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dollarDao(): DollarDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun notificationDao(): NotificationDao
    abstract fun demoDao(): DemoDao
    abstract fun configDao(): ConfigDao
    abstract fun eventDao(): EventDao
    abstract fun remoteConfigHistoryDao(): RemoteConfigHistoryDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>

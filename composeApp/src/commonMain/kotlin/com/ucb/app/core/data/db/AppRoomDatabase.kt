package com.ucb.app.core.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.ucb.app.core.data.db.dao.CartDao
import com.ucb.app.core.data.db.dao.NotificationDao
import com.ucb.app.core.data.db.dao.OrderDao
import com.ucb.app.core.data.db.dao.ProductDao
import com.ucb.app.core.data.db.entity.CartItemEntity
import com.ucb.app.core.data.db.entity.NotificationEntity
import com.ucb.app.core.data.db.entity.OrderEntity
import com.ucb.app.core.data.db.entity.ProductEntity
import com.ucb.app.dollar.data.dao.DollarDao
import com.ucb.app.dollar.data.entity.DollarEntity

@Database(
    entities = [
        DollarEntity::class,
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        NotificationEntity::class
    ],
    version = 2
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dollarDao(): DollarDao

    abstract fun productDao(): ProductDao

    abstract fun cartDao(): CartDao

    abstract fun orderDao(): OrderDao

    abstract fun notificationDao(): NotificationDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>

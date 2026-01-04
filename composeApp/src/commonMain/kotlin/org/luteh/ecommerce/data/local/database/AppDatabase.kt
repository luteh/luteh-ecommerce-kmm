package org.luteh.ecommerce.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.luteh.ecommerce.data.local.database.dao.CartDao
import org.luteh.ecommerce.data.local.database.dao.ShippingAddressDao
import org.luteh.ecommerce.data.local.database.dao.UserSessionDao
import org.luteh.ecommerce.data.local.entity.CartEntity
import org.luteh.ecommerce.data.local.entity.ShippingAddressEntity
import org.luteh.ecommerce.data.local.entity.UserSessionEntity

@Database(
    entities = [CartEntity::class, ShippingAddressEntity::class, UserSessionEntity::class],
    version = 3,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    abstract fun shippingAddressDao(): ShippingAddressDao

    abstract fun userSessionDao(): UserSessionDao
}

@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

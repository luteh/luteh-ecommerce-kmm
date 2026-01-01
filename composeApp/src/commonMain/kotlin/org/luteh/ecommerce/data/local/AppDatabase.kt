package org.luteh.ecommerce.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.luteh.ecommerce.data.local.dao.CartDao
import org.luteh.ecommerce.data.local.dao.ShippingAddressDao
import org.luteh.ecommerce.data.local.entity.CartEntity
import org.luteh.ecommerce.data.local.entity.ShippingAddressEntity

@Database(entities = [CartEntity::class, ShippingAddressEntity::class], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun shippingAddressDao(): ShippingAddressDao
}

@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}


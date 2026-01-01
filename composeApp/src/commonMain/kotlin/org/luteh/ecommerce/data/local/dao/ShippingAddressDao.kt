package org.luteh.ecommerce.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.luteh.ecommerce.data.local.entity.ShippingAddressEntity

@Dao
interface ShippingAddressDao {
    @Query("SELECT * FROM shipping_address WHERE id = 1")
    fun getShippingAddress(): Flow<ShippingAddressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShippingAddress(address: ShippingAddressEntity)
}

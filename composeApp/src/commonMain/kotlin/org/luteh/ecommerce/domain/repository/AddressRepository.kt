package org.luteh.ecommerce.domain.repository

import kotlinx.coroutines.flow.Flow
import org.luteh.ecommerce.domain.model.ShippingAddress

interface AddressRepository {
    fun getLastUsedAddress(): Flow<ShippingAddress?>
    suspend fun saveLastUsedAddress(address: ShippingAddress)
}


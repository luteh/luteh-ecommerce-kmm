package org.luteh.ecommerce.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.luteh.ecommerce.data.local.dao.ShippingAddressDao
import org.luteh.ecommerce.data.local.entity.ShippingAddressEntity
import org.luteh.ecommerce.domain.model.ShippingAddress
import org.luteh.ecommerce.domain.repository.AddressRepository

class AddressRepositoryImpl(
    private val shippingAddressDao: ShippingAddressDao
) : AddressRepository {
    override fun getLastUsedAddress(): Flow<ShippingAddress?> {
        return shippingAddressDao.getShippingAddress().map { entity ->
            entity?.let {
                ShippingAddress(
                    fullName = it.fullName,
                    addressLine = it.addressLine,
                    city = it.city,
                    postalCode = it.postalCode,
                    phoneNumber = it.phoneNumber
                )
            }
        }
    }

    override suspend fun saveLastUsedAddress(address: ShippingAddress) {
        shippingAddressDao.insertShippingAddress(
            ShippingAddressEntity(
                fullName = address.fullName,
                addressLine = address.addressLine,
                city = address.city,
                postalCode = address.postalCode,
                phoneNumber = address.phoneNumber
            )
        )
    }
}


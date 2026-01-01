package org.luteh.ecommerce.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shipping_address")
data class ShippingAddressEntity(
    @PrimaryKey
    val id: Int = 1, // Always 1 for single record
    val fullName: String,
    val addressLine: String,
    val city: String,
    val postalCode: String,
    val phoneNumber: String
)


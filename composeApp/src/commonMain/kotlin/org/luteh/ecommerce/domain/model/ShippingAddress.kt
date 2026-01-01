package org.luteh.ecommerce.domain.model

data class ShippingAddress(
    val fullName: String,
    val addressLine: String,
    val city: String,
    val postalCode: String,
    val phoneNumber: String
)


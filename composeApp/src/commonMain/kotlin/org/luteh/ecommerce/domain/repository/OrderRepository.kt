package org.luteh.ecommerce.domain.repository

import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.model.ShippingAddress

interface OrderRepository {
    suspend fun placeOrder(
        items: List<CartItemModel>,
        shippingAddress: ShippingAddress,
        totalAmount: Double,
    )
}

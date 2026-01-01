package org.luteh.ecommerce.data.repository

import kotlinx.coroutines.delay
import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.model.ShippingAddress
import org.luteh.ecommerce.domain.repository.CartRepository
import org.luteh.ecommerce.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val cartRepository: CartRepository
) : OrderRepository {
    override suspend fun placeOrder(
        items: List<CartItemModel>,
        shippingAddress: ShippingAddress,
        totalAmount: Double
    ) {
        // Simulate network delay
        delay(2000)
        // In a real app, we would send this data to the backend
        // For now, we just clear the cart
        cartRepository.clearCart()
    }
}


package org.luteh.ecommerce.domain.usecase.order

import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.model.ShippingAddress
import org.luteh.ecommerce.domain.repository.OrderRepository

class PlaceOrderUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(items: List<CartItemModel>, shippingAddress: ShippingAddress, totalAmount: Double) {
        orderRepository.placeOrder(items, shippingAddress, totalAmount)
    }
}


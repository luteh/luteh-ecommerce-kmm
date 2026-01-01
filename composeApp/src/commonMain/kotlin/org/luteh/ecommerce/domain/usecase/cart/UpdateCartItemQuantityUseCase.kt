package org.luteh.ecommerce.domain.usecase.cart

import org.luteh.ecommerce.domain.repository.CartRepository

class UpdateCartItemQuantityUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(productId: String, quantity: Int) {
        if (quantity > 0) {
            cartRepository.updateQuantity(productId, quantity)
        } else {
            cartRepository.removeFromCart(productId)
        }
    }
}


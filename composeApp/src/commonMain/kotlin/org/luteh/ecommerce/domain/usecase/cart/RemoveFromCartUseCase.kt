package org.luteh.ecommerce.domain.usecase.cart

import org.luteh.ecommerce.domain.repository.CartRepository

class RemoveFromCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(productId: String) {
        cartRepository.removeFromCart(productId)
    }
}


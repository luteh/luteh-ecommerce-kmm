package org.luteh.ecommerce.domain.usecase.cart

import org.luteh.ecommerce.domain.repository.CartRepository

class ClearCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke() {
        cartRepository.clearCart()
    }
}


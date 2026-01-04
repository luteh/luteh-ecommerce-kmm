package org.luteh.ecommerce.domain.usecase.cart

import org.luteh.ecommerce.domain.repository.CartRepository

interface UpdateCartItemQuantityUseCase {
    suspend operator fun invoke(productId: String, quantity: Int): Result<Unit>
}

class UpdateCartItemQuantityUseCaseImpl(private val cartRepository: CartRepository) :
    UpdateCartItemQuantityUseCase {
    override suspend operator fun invoke(productId: String, quantity: Int) = runCatching {
        if (quantity > 0) {
            cartRepository.updateQuantity(productId, quantity)
        } else {
            cartRepository.removeFromCart(productId)
        }
    }
}

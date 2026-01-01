package org.luteh.ecommerce.domain.usecase.cart

import kotlinx.coroutines.flow.Flow
import org.luteh.ecommerce.domain.repository.CartRepository

class GetCartItemCountUseCase(private val cartRepository: CartRepository) {
    operator fun invoke(): Flow<Int> {
        return cartRepository.getCartItemCount()
    }
}


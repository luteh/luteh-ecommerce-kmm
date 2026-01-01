package org.luteh.ecommerce.domain.usecase.cart

import kotlinx.coroutines.flow.Flow
import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.repository.CartRepository

class GetCartItemsUseCase(private val cartRepository: CartRepository) {
    operator fun invoke(): Flow<List<CartItemModel>> {
        return cartRepository.getCartItems()
    }
}


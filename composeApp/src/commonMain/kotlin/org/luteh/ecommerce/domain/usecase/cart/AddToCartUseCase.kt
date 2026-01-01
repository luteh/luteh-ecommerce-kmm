package org.luteh.ecommerce.domain.usecase.cart

import org.luteh.ecommerce.domain.model.ProductModel
import org.luteh.ecommerce.domain.repository.CartRepository

class AddToCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(product: ProductModel) {
        cartRepository.addToCart(product)
    }
}


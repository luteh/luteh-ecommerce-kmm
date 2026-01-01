package org.luteh.ecommerce.domain.repository

import kotlinx.coroutines.flow.Flow
import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.model.ProductModel

interface CartRepository {
    fun getCartItems(): Flow<List<CartItemModel>>

    suspend fun addToCart(product: ProductModel)

    suspend fun updateQuantity(productId: String, quantity: Int)

    suspend fun removeFromCart(productId: String)

    suspend fun clearCart()

    fun getCartItemCount(): Flow<Int>
}

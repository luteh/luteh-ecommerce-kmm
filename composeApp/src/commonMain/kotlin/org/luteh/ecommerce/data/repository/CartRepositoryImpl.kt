package org.luteh.ecommerce.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.luteh.ecommerce.data.local.dao.CartDao
import org.luteh.ecommerce.data.local.entity.CartEntity
import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.model.ProductModel
import org.luteh.ecommerce.domain.repository.CartRepository

class CartRepositoryImpl(private val cartDao: CartDao) : CartRepository {
    override fun getCartItems(): Flow<List<CartItemModel>> {
        return cartDao.getAllCartItems().map { entities ->
            entities.map { entity ->
                CartItemModel(
                    product =
                        ProductModel(
                            id = entity.productId,
                            name = entity.name,
                            price = entity.price,
                            thumbnailImageUrl = entity.imageUrl ?: "",
                            shopName = "", // Not stored in cart entity
                            rating = 0.0, // Not stored
                            ratingCount = 0, // Not stored
                        ),
                    quantity = entity.quantity,
                )
            }
        }
    }

    override suspend fun addToCart(product: ProductModel) {
        val existingItem = cartDao.getCartItemById(product.id)
        val quantity = existingItem?.quantity?.plus(1) ?: 1
        cartDao.insertOrUpdate(
            CartEntity(
                productId = product.id,
                name = product.name,
                price = product.price,
                quantity = quantity,
                imageUrl = product.thumbnailImageUrl,
                category = "", // Add category to ProductModel if needed or pass it
            )
        )
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) {
        val existingItem = cartDao.getCartItemById(productId)
        if (existingItem != null) {
            cartDao.insertOrUpdate(existingItem.copy(quantity = quantity))
        }
    }

    override suspend fun removeFromCart(productId: String) {
        val existingItem = cartDao.getCartItemById(productId)
        if (existingItem != null) {
            cartDao.delete(existingItem)
        }
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }

    override fun getCartItemCount(): Flow<Int> {
        return cartDao.getCartItemCount()
    }
}

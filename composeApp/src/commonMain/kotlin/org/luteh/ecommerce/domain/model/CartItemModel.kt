package org.luteh.ecommerce.domain.model

data class CartItemModel(
    val product: ProductModel,
    val quantity: Int
)


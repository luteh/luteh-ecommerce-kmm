package org.luteh.ecommerce.domain.model

data class ProductModel(
    val id: String,
    val thumbnailImageUrl: String,
    val name: String,
    val price: Double,
    val shopName: String,
    val rating: Double,
    val ratingCount: Int
)
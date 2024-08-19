package org.luteh.ecommerce.domain.model

data class ProductDetailModel(
    val id: String,
    val thumbnailImageUrl: String,
    val name: String,
    val price: Double,
    val shopName: String,
    val rating: Double,
    val ratingCount: Int,
    val description: String,
    val imageUrls: List<String>,
) {
    companion object {
        val dummy
            get() = ProductDetailModel(
                id = "habeo",
                thumbnailImageUrl = "https://search.yahoo.com/search?p=melius",
                name = "Verna Murray",
                price = 16.17,
                shopName = "Bobbie Davis",
                rating = 18.19,
                ratingCount = 5025,
                description = "volumus",
                imageUrls = listOf()
            )
    }
}
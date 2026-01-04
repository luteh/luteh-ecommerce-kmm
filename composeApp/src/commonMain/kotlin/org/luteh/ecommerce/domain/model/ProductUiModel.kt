package org.luteh.ecommerce.domain.model

import androidx.compose.ui.graphics.Color

data class ProductUiModel(
    val id: String,
    val name: String,
    val price: String,
    val rating: Double,
    val imageColor: Color,
    val category: String = "All",
    val description: String =
        "Experience premium quality with this outstanding product. Designed for comfort and durability, it features state-of-the-art materials and a sleek modern design perfect for any occasion.",
)

val dummyProducts =
    listOf(
        ProductUiModel(
            "1",
            "Wireless Headphones",
            "$129.99",
            4.5,
            Color(0xFFE57373),
            "Electronics",
        ),
        ProductUiModel(
            "2",
            "Smart Watch Series 7",
            "$399.00",
            4.8,
            Color(0xFF81C784),
            "Electronics",
        ),
        ProductUiModel("3", "Running Shoes", "$89.95", 4.3, Color(0xFF64B5F6), "Fashion"),
        ProductUiModel("4", "Cotton T-Shirt", "$24.99", 4.1, Color(0xFFFFD54F), "Fashion"),
        ProductUiModel("5", "Leather Backpack", "$149.50", 4.7, Color(0xFFBA68C8), "Fashion"),
        ProductUiModel("6", "Sunglasses", "$59.00", 4.4, Color(0xFF4DB6AC), "Fashion"),
        ProductUiModel("7", "Gaming Mouse", "$49.99", 4.6, Color(0xFF9575CD), "Electronics"),
        ProductUiModel(
            "8",
            "Mechanical Keyboard",
            "$119.00",
            4.7,
            Color(0xFF4DD0E1),
            "Electronics",
        ),
        ProductUiModel("9", "Laptop Stand", "$29.99", 4.2, Color(0xFFAED581), "Electronics"),
        ProductUiModel("10", "Water Bottle", "$19.99", 4.5, Color(0xFFFF8A65), "Home"),
    )

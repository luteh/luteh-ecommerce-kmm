package org.luteh.ecommerce.presentation.ui.common

import androidx.compose.ui.graphics.Color

data class ProductUiModel(
    val name: String,
    val price: String,
    val rating: Double,
    val imageColor: Color,
    val category: String = "All"
)

val dummyProducts = listOf(
    ProductUiModel("Wireless Headphones", "$129.99", 4.5, Color(0xFFE57373), "Electronics"),
    ProductUiModel("Smart Watch Series 7", "$399.00", 4.8, Color(0xFF81C784), "Electronics"),
    ProductUiModel("Running Shoes", "$89.95", 4.3, Color(0xFF64B5F6), "Fashion"),
    ProductUiModel("Cotton T-Shirt", "$24.99", 4.1, Color(0xFFFFD54F), "Fashion"),
    ProductUiModel("Leather Backpack", "$149.50", 4.7, Color(0xFFBA68C8), "Fashion"),
    ProductUiModel("Sunglasses", "$59.00", 4.4, Color(0xFF4DB6AC), "Fashion"),
    ProductUiModel("Gaming Mouse", "$49.99", 4.6, Color(0xFF9575CD), "Electronics"),
    ProductUiModel("Mechanical Keyboard", "$119.00", 4.7, Color(0xFF4DD0E1), "Electronics"),
    ProductUiModel("Laptop Stand", "$29.99", 4.2, Color(0xFFAED581), "Electronics"),
    ProductUiModel("Water Bottle", "$19.99", 4.5, Color(0xFFFF8A65), "Home"),
)


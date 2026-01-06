package org.luteh.ecommerce.domain.model

import androidx.compose.ui.graphics.Color

data class BannerModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val buttonText: String,
    val imageUrl: String?,
    val backgroundColor: Color,
    val textColor: Color,
) {
    companion object {
        val dummies =
            listOf(
                BannerModel(
                    id = "1",
                    title = "Summer Sale",
                    subtitle = "Get up to 50% off on selected items",
                    buttonText = "Shop Now",
                    imageUrl = "https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=400",
                    backgroundColor = Color(0xFF6750A4),
                    textColor = Color.White,
                ),
                BannerModel(
                    id = "2",
                    title = "New Arrivals",
                    subtitle = "Check out the latest fashion trends",
                    buttonText = "Explore",
                    imageUrl = "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400",
                    backgroundColor = Color(0xFF006D77),
                    textColor = Color.White,
                ),
                BannerModel(
                    id = "3",
                    title = "Flash Deal",
                    subtitle = "Limited time offers - Don't miss out!",
                    buttonText = "View Deals",
                    imageUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=400",
                    backgroundColor = Color(0xFFE07A5F),
                    textColor = Color.White,
                ),
                BannerModel(
                    id = "4",
                    title = "Free Shipping",
                    subtitle = "On orders over \$50 - Shop today!",
                    buttonText = "Learn More",
                    imageUrl = "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=400",
                    backgroundColor = Color(0xFF2A9D8F),
                    textColor = Color.White,
                ),
            )
    }
}

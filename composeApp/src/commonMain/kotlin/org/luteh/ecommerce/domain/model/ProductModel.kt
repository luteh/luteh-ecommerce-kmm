package org.luteh.ecommerce.domain.model

data class ProductModel(
    val id: String,
    val image: ProductImage,
    val name: String,
    val price: Double,
    val shopName: String,
    val rating: Double,
    val ratingCount: Int,
    val description: String,
    val category: String,
) {
    companion object {
        val dummies
            get() =
                listOf(
                    ProductModel(
                        id = "1",
                        image =
                            ProductImage(
                                id = "img1",
                                thumbnailUrl =
                                    "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400",
                                imageUrls =
                                    listOf(
                                        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e",
                                        "https://images.unsplash.com/photo-1484704849700-f032a568e944",
                                        "https://images.unsplash.com/photo-1524678606370-a47ad25cb82a",
                                    ),
                            ),
                        name = "Wireless Headphones",
                        price = 299.99,
                        shopName = "TechStore Pro",
                        rating = 4.8,
                        ratingCount = 1523,
                        description =
                            "Premium wireless headphones with active noise cancellation and 30-hour battery life.",
                        category = "Electronics",
                    ),
                    ProductModel(
                        id = "2",
                        image =
                            ProductImage(
                                id = "img2",
                                thumbnailUrl =
                                    "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400",
                                imageUrls =
                                    listOf(
                                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30",
                                        "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1",
                                        "https://images.unsplash.com/photo-1579586337278-3befd40fd17a",
                                    ),
                            ),
                        name = "Smart Watch Ultra",
                        price = 599.00,
                        shopName = "Gadget Hub",
                        rating = 4.7,
                        ratingCount = 2341,
                        description =
                            "Advanced fitness tracking, heart rate monitor, GPS, and water resistant up to 50m.",
                        category = "Electronics",
                    ),
                    ProductModel(
                        id = "3",
                        image =
                            ProductImage(
                                id = "img3",
                                thumbnailUrl =
                                    "https://images.unsplash.com/photo-1560343090-f0409e92791a?w=400",
                                imageUrls =
                                    listOf(
                                        "https://images.unsplash.com/photo-1560343090-f0409e92791a",
                                        "https://images.unsplash.com/photo-1542291026-7eec264c27ff",
                                        "https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa",
                                    ),
                            ),
                        name = "Running Shoes Air Max",
                        price = 149.99,
                        shopName = "SportZone",
                        rating = 4.6,
                        ratingCount = 987,
                        description =
                            "Lightweight running shoes with superior cushioning and breathable mesh upper.",
                        category = "Sports",
                    ),
                    ProductModel(
                        id = "4",
                        image =
                            ProductImage(
                                id = "img4",
                                thumbnailUrl =
                                    "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400",
                                imageUrls =
                                    listOf(
                                        "https://images.unsplash.com/photo-1553062407-98eeb64c6a62",
                                        "https://images.unsplash.com/photo-1548036328-c9fa89d128fa",
                                        "https://images.unsplash.com/photo-1622560480605-d83c853bc5c3",
                                    ),
                            ),
                        name = "Leather Backpack",
                        price = 89.50,
                        shopName = "Urban Style",
                        rating = 4.5,
                        ratingCount = 456,
                        description =
                            "Genuine leather backpack with laptop compartment and multiple pockets.",
                        category = "Fashion",
                    ),
                    ProductModel(
                        id = "5",
                        image =
                            ProductImage(
                                id = "img5",
                                thumbnailUrl =
                                    "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=400",
                                imageUrls =
                                    listOf(
                                        "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f",
                                        "https://images.unsplash.com/photo-1606980702414-f4f29c932f1a",
                                        "https://images.unsplash.com/photo-1502920917128-1aa500764cbd",
                                    ),
                            ),
                        name = "Professional Camera",
                        price = 1299.00,
                        shopName = "PhotoWorld",
                        rating = 4.9,
                        ratingCount = 678,
                        description =
                            "Mirrorless camera with 4K video, 24MP sensor, and professional lens kit.",
                        category = "Electronics",
                    ),
                    ProductModel(
                        id = "6",
                        image =
                            ProductImage(
                                id = "img6",
                                thumbnailUrl =
                                    "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400",
                                imageUrls =
                                    listOf(
                                        "https://images.unsplash.com/photo-1572635196237-14b3f281503f",
                                        "https://images.unsplash.com/photo-1511499767150-a48a237f0083",
                                        "https://images.unsplash.com/photo-1473496169904-658ba7c44d8a",
                                    ),
                            ),
                        name = "Designer Sunglasses",
                        price = 199.99,
                        shopName = "Fashion Forward",
                        rating = 4.4,
                        ratingCount = 834,
                        description =
                            "UV protection sunglasses with polarized lenses and titanium frame.",
                        category = "Fashion",
                    ),
                    ProductModel(
                        id = "7",
                        image =
                            ProductImage(
                                id = "img7",
                                thumbnailUrl =
                                    "https://images.unsplash.com/photo-1585386959984-a4155224a1ad?w=400",
                                imageUrls =
                                    listOf(
                                        "https://images.unsplash.com/photo-1585386959984-a4155224a1ad",
                                        "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f",
                                        "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b",
                                    ),
                            ),
                        name = "Yoga Mat Premium",
                        price = 45.00,
                        shopName = "FitLife Store",
                        rating = 4.7,
                        ratingCount = 1245,
                        description =
                            "Eco-friendly yoga mat with extra cushioning and non-slip surface.",
                        category = "Sports",
                    ),
                    ProductModel(
                        id = "8",
                        image =
                            ProductImage(
                                id = "img8",
                                thumbnailUrl =
                                    "https://images.unsplash.com/photo-1585298723682-7115561c51b7?w=400",
                                imageUrls =
                                    listOf(
                                        "https://images.unsplash.com/photo-1585298723682-7115561c51b7",
                                        "https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6",
                                        "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085",
                                    ),
                            ),
                        name = "Coffee Maker Deluxe",
                        price = 179.99,
                        shopName = "Home Essentials",
                        rating = 4.6,
                        ratingCount = 567,
                        description =
                            "Programmable coffee maker with thermal carafe and built-in grinder.",
                        category = "Home",
                    ),
                    ProductModel(
                        id = "9",
                        image =
                            ProductImage(
                                id = "img9",
                                thumbnailUrl =
                                    "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=400",
                                imageUrls =
                                    listOf(
                                        "https://images.unsplash.com/photo-1546868871-7041f2a55e12",
                                        "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1",
                                        "https://images.unsplash.com/photo-1545454675-3531b543be5d",
                                    ),
                            ),
                        name = "Bluetooth Speaker",
                        price = 79.99,
                        shopName = "AudioMax",
                        rating = 4.5,
                        ratingCount = 1891,
                        description =
                            "Portable waterproof speaker with 360-degree sound and 12-hour battery.",
                        category = "Electronics",
                    ),
                    ProductModel(
                        id = "10",
                        image =
                            ProductImage(
                                id = "img10",
                                thumbnailUrl =
                                    "https://images.unsplash.com/photo-1556656793-08538906a9f8?w=400",
                                imageUrls =
                                    listOf(
                                        "https://images.unsplash.com/photo-1556656793-08538906a9f8",
                                        "https://images.unsplash.com/photo-1507473885765-e6ed057f782c",
                                        "https://images.unsplash.com/photo-1513506003901-1e6a229e2d15",
                                    ),
                            ),
                        name = "Desk Lamp LED",
                        price = 39.99,
                        shopName = "Office Plus",
                        rating = 4.3,
                        ratingCount = 723,
                        description =
                            "Adjustable LED desk lamp with touch control and multiple brightness levels.",
                        category = "Home",
                    ),
                )
    }
}

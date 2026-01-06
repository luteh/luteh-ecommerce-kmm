package org.luteh.ecommerce.data.repository

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.model.ProductModel
import org.luteh.ecommerce.domain.model.ShippingAddress
import org.luteh.ecommerce.domain.repository.CartRepository
import kotlin.test.BeforeTest
import kotlin.test.Test

class OrderRepositoryImplTest {

    private lateinit var repository: OrderRepositoryImpl
    private lateinit var cartRepository: CartRepository

    @BeforeTest
    fun setUp() {
        cartRepository = mock(MockMode.autoUnit)
        repository = OrderRepositoryImpl(cartRepository)
    }

    @Test
    fun `placeOrder should clear cart after delay`() = runTest {
        val items =
            listOf(
                CartItemModel(
                    product =
                        ProductModel(
                            id = "1",
                            name = "Product",
                            price = 10.0,
                            imageUrl = "url",
                            shopName = "Shop",
                            rating = 5.0,
                            ratingCount = 10,
                        ),
                    quantity = 1,
                )
            )
        val address = ShippingAddress("Name", "Address", "City", "12345", "123")

        everySuspend { cartRepository.clearCart() } returns Unit

        repository.placeOrder(items, address, 10.0)

        verifySuspend { cartRepository.clearCart() }
    }
}

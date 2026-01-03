package org.luteh.ecommerce.domain.usecase.cart

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.domain.repository.CartRepository
import kotlin.test.BeforeTest
import kotlin.test.Test

class UpdateCartItemQuantityUseCaseTest {

    private lateinit var useCase: UpdateCartItemQuantityUseCase
    private lateinit var cartRepository: CartRepository

    @BeforeTest
    fun setUp() {
        cartRepository = mock(MockMode.autoUnit)
        useCase = UpdateCartItemQuantityUseCase(cartRepository)
    }

    @Test
    fun `invoke with positive quantity should call updateQuantity`() = runTest {
        val productId = "1"
        val quantity = 5
        everySuspend { cartRepository.updateQuantity(productId, quantity) } returns Unit

        useCase(productId, quantity)

        verifySuspend { cartRepository.updateQuantity(productId, quantity) }
    }

    @Test
    fun `invoke with zero quantity should call removeFromCart`() = runTest {
        val productId = "1"
        val quantity = 0
        everySuspend { cartRepository.removeFromCart(productId) } returns Unit

        useCase(productId, quantity)

        verifySuspend { cartRepository.removeFromCart(productId) }
    }

    @Test
    fun `invoke with negative quantity should call removeFromCart`() = runTest {
        val productId = "1"
        val quantity = -1
        everySuspend { cartRepository.removeFromCart(productId) } returns Unit

        useCase(productId, quantity)

        verifySuspend { cartRepository.removeFromCart(productId) }
    }
}

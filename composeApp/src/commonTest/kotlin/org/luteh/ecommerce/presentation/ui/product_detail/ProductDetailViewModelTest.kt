package org.luteh.ecommerce.presentation.ui.product_detail

import app.cash.turbine.test
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.domain.repository.CartRepository
import org.luteh.ecommerce.domain.usecase.auth.CheckSessionUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var cartRepository: CartRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var checkSessionUseCase: CheckSessionUseCase

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cartRepository = mock(MockMode.autoUnit)
        authRepository = mock(MockMode.autoUnit)
        checkSessionUseCase = CheckSessionUseCase(authRepository)
        viewModel = ProductDetailViewModel(cartRepository, checkSessionUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `LoadProduct with valid ID should update state with product`() = runTest {
        viewModel.onEvent(ProductDetailEvent.LoadProduct("1"))

        val state = viewModel.state.value
        assertNotNull(state.product)
        assertEquals("1", state.product.id)
        assertEquals("Wireless Headphones", state.product.name)
    }

    @Test
    fun `LoadProduct with invalid ID should not update state`() = runTest {
        viewModel.onEvent(ProductDetailEvent.LoadProduct("999"))

        val state = viewModel.state.value
        assertNull(state.product)
    }

    @Test
    fun `AddToCart when logged in should add to cart and show snackbar`() = runTest {
        // Arrange
        everySuspend { authRepository.getLoginSession() } returns true
        everySuspend { cartRepository.addToCart(any()) } returns Unit

        // Load product first so we have something to add
        viewModel.onEvent(ProductDetailEvent.LoadProduct("1"))

        viewModel.effect.test {
            // Act
            viewModel.onEvent(ProductDetailEvent.AddToCart)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            val effect = awaitItem()
            assertTrue(effect is ProductDetailEffect.ShowSnackbar)
            assertEquals("Added to cart", effect.message)

            assertTrue(viewModel.state.value.isAddedToCart)

            verifySuspend { cartRepository.addToCart(any()) }
        }
    }

    @Test
    fun `AddToCart when not logged in should navigate to login`() = runTest {
        // Arrange
        everySuspend { authRepository.getLoginSession() } returns false

        viewModel.effect.test {
            // Act
            viewModel.onEvent(ProductDetailEvent.AddToCart)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            val effect = awaitItem()
            assertEquals(ProductDetailEffect.NavigateToLogin, effect)
        }
    }

    @Test
    fun `OnCartClicked when logged in should navigate to cart`() = runTest {
        // Arrange
        everySuspend { authRepository.getLoginSession() } returns true

        viewModel.effect.test {
            // Act
            viewModel.onEvent(ProductDetailEvent.OnCartClicked)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            val effect = awaitItem()
            assertEquals(ProductDetailEffect.NavigateToCart, effect)
        }
    }

    @Test
    fun `OnCartClicked when not logged in should navigate to login`() = runTest {
        // Arrange
        everySuspend { authRepository.getLoginSession() } returns false

        viewModel.effect.test {
            // Act
            viewModel.onEvent(ProductDetailEvent.OnCartClicked)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            val effect = awaitItem()
            assertEquals(ProductDetailEffect.NavigateToLogin, effect)
        }
    }
}

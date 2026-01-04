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
import org.luteh.ecommerce.domain.model.SessionResult
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
    private lateinit var checkSessionUseCase: CheckSessionUseCase

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cartRepository = mock(MockMode.autoUnit)
        checkSessionUseCase = mock(MockMode.autoUnit)
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
        everySuspend { checkSessionUseCase() } returns Result.success(SessionResult.LoggedIn)
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
        everySuspend { checkSessionUseCase() } returns Result.success(SessionResult.NotLoggedIn)

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
        everySuspend { checkSessionUseCase() } returns Result.success(SessionResult.LoggedIn)

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
        everySuspend { checkSessionUseCase() } returns Result.success(SessionResult.NotLoggedIn)

        viewModel.effect.test {
            // Act
            viewModel.onEvent(ProductDetailEvent.OnCartClicked)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            val effect = awaitItem()
            assertEquals(ProductDetailEffect.NavigateToLogin, effect)
        }
    }

    @Test
    fun `AddToCart when session check fails should show snackbar`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        everySuspend { checkSessionUseCase() } returns
            Result.failure(RuntimeException(errorMessage))

        viewModel.effect.test {
            // Act
            viewModel.onEvent(ProductDetailEvent.AddToCart)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            val effect = awaitItem()
            assertTrue(effect is ProductDetailEffect.ShowSnackbar)
            assertEquals(errorMessage, effect.message)
        }
    }

    @Test
    fun `OnCartClicked when session check fails should show snackbar`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        everySuspend { checkSessionUseCase() } returns
            Result.failure(RuntimeException(errorMessage))

        viewModel.effect.test {
            // Act
            viewModel.onEvent(ProductDetailEvent.OnCartClicked)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            val effect = awaitItem()
            assertTrue(effect is ProductDetailEffect.ShowSnackbar)
            assertEquals(errorMessage, effect.message)
        }
    }

    @Test
    fun `AddToCart when product is not loaded should do nothing`() = runTest {
        // Arrange
        everySuspend { checkSessionUseCase() } returns Result.success(SessionResult.LoggedIn)

        viewModel.effect.test {
            // Act
            viewModel.onEvent(ProductDetailEvent.AddToCart)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            expectNoEvents()
        }
    }
}

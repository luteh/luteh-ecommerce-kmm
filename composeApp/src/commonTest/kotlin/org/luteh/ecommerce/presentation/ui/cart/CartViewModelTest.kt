package org.luteh.ecommerce.presentation.ui.cart

import app.cash.turbine.test
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.model.ProductModel
import org.luteh.ecommerce.domain.repository.CartRepository
import org.luteh.ecommerce.domain.usecase.cart.UpdateCartItemQuantityUseCase
import org.luteh.ecommerce.presentation.core.ResultState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: CartRepository
    private lateinit var updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cartRepository = mock(MockMode.autoUnit)
        updateCartItemQuantityUseCase = mock(MockMode.autoUnit)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = CartViewModel(cartRepository, updateCartItemQuantityUseCase)
    }

    @Test
    fun `init should load cart items and update state`() = runTest {
        val product = ProductModel("1", "url", "Product 1", 100.0, "Shop", 4.5, 10)
        val cartItems = listOf(CartItemModel(product, 2))
        everySuspend { cartRepository.getCartItems() } returns flowOf(cartItems)

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.cartItemsState is ResultState.Success)
        assertEquals(cartItems, state.cartItemsState.data)
        assertEquals(200.0, state.totalPrice)
    }

    @Test
    fun `init should handle error when loading cart items fails`() = runTest {
        val exception = RuntimeException("Network error")
        everySuspend { cartRepository.getCartItems() } throws exception

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.cartItemsState is ResultState.Error)
        assertEquals(exception, state.cartItemsState.exception)
    }

    @Test
    fun `OnUpdateQuantity should call use case`() = runTest {
        everySuspend { cartRepository.getCartItems() } returns flowOf(emptyList())
        everySuspend { updateCartItemQuantityUseCase("1", 5) } returns Result.success(Unit)
        createViewModel()

        viewModel.processEvent(CartViewModel.Event.OnUpdateQuantity("1", 5))
        testDispatcher.scheduler.advanceUntilIdle()

        verifySuspend { updateCartItemQuantityUseCase("1", 5) }
    }

    @Test
    fun `OnUpdateQuantity failure should show toast`() = runTest {
        everySuspend { cartRepository.getCartItems() } returns flowOf(emptyList())
        val exception = RuntimeException("Update failed")
        everySuspend { updateCartItemQuantityUseCase("1", 5) } returns Result.failure(exception)
        createViewModel()

        viewModel.effect.test {
            viewModel.processEvent(CartViewModel.Event.OnUpdateQuantity("1", 5))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is CartViewModel.Effect.ShowToast)
            assertEquals("Failed to update quantity: Update failed", effect.message)
        }
    }

    @Test
    fun `OnRemoveItem success should call repository and show toast`() = runTest {
        everySuspend { cartRepository.getCartItems() } returns flowOf(emptyList())
        everySuspend { cartRepository.removeFromCart("1") } returns Unit
        createViewModel()

        viewModel.effect.test {
            viewModel.processEvent(CartViewModel.Event.OnRemoveItem("1"))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is CartViewModel.Effect.ShowToast)
            assertEquals("Item removed", effect.message)
            verifySuspend { cartRepository.removeFromCart("1") }
        }
    }

    @Test
    fun `OnRemoveItem failure should show toast`() = runTest {
        everySuspend { cartRepository.getCartItems() } returns flowOf(emptyList())
        val exception = RuntimeException("Remove failed")
        everySuspend { cartRepository.removeFromCart("1") } throws exception
        createViewModel()

        viewModel.effect.test {
            viewModel.processEvent(CartViewModel.Event.OnRemoveItem("1"))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is CartViewModel.Effect.ShowToast)
            assertEquals("Failed to remove item: Remove failed", effect.message)
        }
    }

    @Test
    fun `OnClearCart success should call repository and show toast`() = runTest {
        everySuspend { cartRepository.getCartItems() } returns flowOf(emptyList())
        everySuspend { cartRepository.clearCart() } returns Unit
        createViewModel()

        viewModel.effect.test {
            viewModel.processEvent(CartViewModel.Event.OnClearCart)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is CartViewModel.Effect.ShowToast)
            assertEquals("Cart cleared", effect.message)
            verifySuspend { cartRepository.clearCart() }
        }
    }

    @Test
    fun `OnClearCart failure should show toast`() = runTest {
        everySuspend { cartRepository.getCartItems() } returns flowOf(emptyList())
        val exception = RuntimeException("Clear failed")
        everySuspend { cartRepository.clearCart() } throws exception
        createViewModel()

        viewModel.effect.test {
            viewModel.processEvent(CartViewModel.Event.OnClearCart)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is CartViewModel.Effect.ShowToast)
            assertEquals("Failed to clear cart: Clear failed", effect.message)
        }
    }

    @Test
    fun `OnCheckout should navigate to checkout`() = runTest {
        everySuspend { cartRepository.getCartItems() } returns flowOf(emptyList())
        createViewModel()

        viewModel.effect.test {
            viewModel.processEvent(CartViewModel.Event.OnCheckout)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(CartViewModel.Effect.NavigateToCheckout, awaitItem())
        }
    }

    @Test
    fun `OnNavigateBack should navigate back`() = runTest {
        everySuspend { cartRepository.getCartItems() } returns flowOf(emptyList())
        createViewModel()

        viewModel.effect.test {
            viewModel.processEvent(CartViewModel.Event.OnNavigateBack)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(CartViewModel.Effect.NavigateBack, awaitItem())
        }
    }
}

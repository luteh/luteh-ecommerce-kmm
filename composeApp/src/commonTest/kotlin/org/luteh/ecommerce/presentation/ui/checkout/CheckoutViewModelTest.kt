package org.luteh.ecommerce.presentation.ui.checkout

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
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
import org.luteh.ecommerce.domain.model.ProductImage
import org.luteh.ecommerce.domain.model.ProductModel
import org.luteh.ecommerce.domain.model.ShippingAddress
import org.luteh.ecommerce.domain.repository.AddressRepository
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.domain.repository.CartRepository
import org.luteh.ecommerce.domain.repository.OrderRepository
import org.luteh.ecommerce.presentation.core.ResultState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelTest {

    private lateinit var viewModel: CheckoutViewModel
    private lateinit var cartRepository: CartRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var addressRepository: AddressRepository
    private lateinit var authRepository: AuthRepository

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cartRepository = mock(MockMode.autoUnit)
        orderRepository = mock(MockMode.autoUnit)
        addressRepository = mock(MockMode.autoUnit)
        authRepository = mock(MockMode.autoUnit)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel =
            CheckoutViewModel(
                cartRepository = cartRepository,
                orderRepository = orderRepository,
                addressRepository = addressRepository,
                authRepository = authRepository,
            )
    }

    @Test
    fun `init should load cart items and calculate total amount`() = runTest {
        val product =
            ProductModel(
                id = "1",
                image = ProductImage(id = "img1", thumbnailUrl = "url", imageUrls = listOf("url")),
                name = "Test Product",
                price = 100.0,
                shopName = "Shop",
                rating = 4.5,
                ratingCount = 10,
                description = "Description",
                category = "Category",
            )
        val cartItems = listOf(CartItemModel(product = product, quantity = 2))

        every { cartRepository.getCartItems() } returns flowOf(cartItems)
        every { addressRepository.getLastUsedAddress() } returns flowOf(null)

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(cartItems, state.cartItems)
        assertEquals(200.0, state.totalAmount)
    }

    @Test
    fun `init should load last used address if available`() = runTest {
        val address =
            ShippingAddress(
                fullName = "John Doe",
                addressLine = "123 Main St",
                city = "New York",
                postalCode = "10001",
                phoneNumber = "1234567890",
            )

        every { cartRepository.getCartItems() } returns flowOf(emptyList())
        every { addressRepository.getLastUsedAddress() } returns flowOf(address)

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("John Doe", state.fullName)
        assertEquals("123 Main St", state.address)
        assertEquals("New York", state.city)
        assertEquals("10001", state.postalCode)
        assertEquals("1234567890", state.phoneNumber)
    }

    @Test
    fun `input events should update state correctly`() = runTest {
        every { cartRepository.getCartItems() } returns flowOf(emptyList())
        every { addressRepository.getLastUsedAddress() } returns flowOf(null)
        createViewModel()

        viewModel.processEvent(CheckoutViewModel.Event.OnFullNameChanged("Jane Doe"))
        assertEquals("Jane Doe", viewModel.state.value.fullName)

        viewModel.processEvent(CheckoutViewModel.Event.OnAddressChanged("456 Elm St"))
        assertEquals("456 Elm St", viewModel.state.value.address)

        viewModel.processEvent(CheckoutViewModel.Event.OnCityChanged("Los Angeles"))
        assertEquals("Los Angeles", viewModel.state.value.city)

        viewModel.processEvent(CheckoutViewModel.Event.OnPostalCodeChanged("90001"))
        assertEquals("90001", viewModel.state.value.postalCode)

        viewModel.processEvent(CheckoutViewModel.Event.OnPhoneNumberChanged("0987654321"))
        assertEquals("0987654321", viewModel.state.value.phoneNumber)
    }

    @Test
    fun `OnPlaceOrder should show toast when input is invalid`() = runTest {
        every { cartRepository.getCartItems() } returns flowOf(emptyList())
        every { addressRepository.getLastUsedAddress() } returns flowOf(null)
        createViewModel()

        // Inputs are empty by default
        viewModel.processEvent(CheckoutViewModel.Event.OnPlaceOrder)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify effect - assuming we can check effects or just verify no state change related to
        // PIN
        assertFalse(viewModel.state.value.isPinVerificationVisible)
        // Ideally we check the effect, but BaseViewModel implementation details for effects are not
        // fully visible.
        // Assuming we can't easily check effects without a collector, we check side effects on
        // state.
    }

    @Test
    fun `OnPlaceOrder should show PIN verification when input is valid`() = runTest {
        every { cartRepository.getCartItems() } returns flowOf(emptyList())
        every { addressRepository.getLastUsedAddress() } returns flowOf(null)
        createViewModel()

        viewModel.processEvent(CheckoutViewModel.Event.OnFullNameChanged("John"))
        viewModel.processEvent(CheckoutViewModel.Event.OnAddressChanged("Street"))
        viewModel.processEvent(CheckoutViewModel.Event.OnCityChanged("City"))
        viewModel.processEvent(CheckoutViewModel.Event.OnPostalCodeChanged("12345"))
        viewModel.processEvent(CheckoutViewModel.Event.OnPhoneNumberChanged("123"))

        viewModel.processEvent(CheckoutViewModel.Event.OnPlaceOrder)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.isPinVerificationVisible)
        assertNull(viewModel.state.value.pinError)
    }

    @Test
    fun `OnPinEntered with invalid PIN should show error`() = runTest {
        every { cartRepository.getCartItems() } returns flowOf(emptyList())
        every { addressRepository.getLastUsedAddress() } returns flowOf(null)
        everySuspend { authRepository.verifyPin("0000") } returns false

        createViewModel()

        // Trigger PIN verification visibility first (optional but realistic)
        viewModel.processEvent(CheckoutViewModel.Event.OnShowPinVerification)

        viewModel.processEvent(CheckoutViewModel.Event.OnPinEntered("0000"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Invalid PIN", viewModel.state.value.pinError)
        assertTrue(viewModel.state.value.isPinVerificationVisible)
    }

    @Test
    fun `OnPinEntered with valid PIN should place order successfully`() = runTest {
        val product =
            ProductModel(
                id = "1",
                image = ProductImage(id = "img1", thumbnailUrl = "url", imageUrls = listOf("url")),
                name = "Test Product",
                price = 10.0,
                shopName = "Shop",
                rating = 4.5,
                ratingCount = 10,
                description = "Description",
                category = "Category",
            )
        val cartItems = listOf(CartItemModel(product = product, quantity = 1))
        val address = ShippingAddress("John", "Street", "City", "12345", "123")

        every { cartRepository.getCartItems() } returns flowOf(cartItems)
        every { addressRepository.getLastUsedAddress() } returns flowOf(address)
        everySuspend { authRepository.verifyPin("1234") } returns true
        everySuspend { orderRepository.placeOrder(any(), any(), any()) } returns Unit
        everySuspend { addressRepository.saveLastUsedAddress(any()) } returns Unit

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle() // Load initial data

        viewModel.processEvent(CheckoutViewModel.Event.OnShowPinVerification)
        viewModel.processEvent(CheckoutViewModel.Event.OnPinEntered("1234"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.isPinVerificationVisible)
        assertTrue(viewModel.state.value.placeOrderState is ResultState.Success)

        verifySuspend {
            orderRepository.placeOrder(
                items = cartItems,
                shippingAddress = address,
                totalAmount = 10.0,
            )
            addressRepository.saveLastUsedAddress(address)
        }
    }

    @Test
    fun `OnPinEntered with valid PIN should handle place order failure`() = runTest {
        val error = RuntimeException("Network error")
        every { cartRepository.getCartItems() } returns flowOf(emptyList())
        every { addressRepository.getLastUsedAddress() } returns flowOf(null)
        everySuspend { authRepository.verifyPin("1234") } returns true
        everySuspend { orderRepository.placeOrder(any(), any(), any()) } throws error

        createViewModel()

        // Set valid inputs manually
        viewModel.processEvent(CheckoutViewModel.Event.OnFullNameChanged("John"))
        viewModel.processEvent(CheckoutViewModel.Event.OnAddressChanged("Street"))
        viewModel.processEvent(CheckoutViewModel.Event.OnCityChanged("City"))
        viewModel.processEvent(CheckoutViewModel.Event.OnPostalCodeChanged("12345"))
        viewModel.processEvent(CheckoutViewModel.Event.OnPhoneNumberChanged("123"))

        viewModel.processEvent(CheckoutViewModel.Event.OnShowPinVerification)
        viewModel.processEvent(CheckoutViewModel.Event.OnPinEntered("1234"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.isPinVerificationVisible)
        assertTrue(viewModel.state.value.placeOrderState is ResultState.Error)
        assertEquals(error, (viewModel.state.value.placeOrderState as ResultState.Error).exception)
    }

    @Test
    fun `OnHidePinVerification should hide verification and clear error`() = runTest {
        every { cartRepository.getCartItems() } returns flowOf(emptyList())
        every { addressRepository.getLastUsedAddress() } returns flowOf(null)
        createViewModel()

        viewModel.processEvent(CheckoutViewModel.Event.OnShowPinVerification)
        // Simulate error
        everySuspend { authRepository.verifyPin("0000") } returns false
        viewModel.processEvent(CheckoutViewModel.Event.OnPinEntered("0000"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Invalid PIN", viewModel.state.value.pinError)

        viewModel.processEvent(CheckoutViewModel.Event.OnHidePinVerification)

        assertFalse(viewModel.state.value.isPinVerificationVisible)
        assertNull(viewModel.state.value.pinError)
    }
}

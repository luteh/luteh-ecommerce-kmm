package org.luteh.ecommerce.presentation.ui.checkout

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.model.ShippingAddress
import org.luteh.ecommerce.domain.repository.AddressRepository
import org.luteh.ecommerce.domain.repository.CartRepository
import org.luteh.ecommerce.domain.repository.OrderRepository
import org.luteh.ecommerce.presentation.core.BaseViewModel
import org.luteh.ecommerce.presentation.core.ResultState

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val addressRepository: AddressRepository
) : BaseViewModel<CheckoutViewModel.State, CheckoutViewModel.Event, CheckoutViewModel.Effect>(State()) {

    init {
        loadCartItems()
        loadLastUsedAddress()
    }

    private fun loadLastUsedAddress() {
        viewModelScope.launch {
            addressRepository.getLastUsedAddress().collectLatest { address ->
                address?.let {
                    updateState { state ->
                        state.copy(
                            fullName = it.fullName,
                            address = it.addressLine,
                            city = it.city,
                            postalCode = it.postalCode,
                            phoneNumber = it.phoneNumber
                        )
                    }
                }
            }
        }
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            cartRepository.getCartItems().collectLatest { items ->
                updateState {
                    it.copy(
                        cartItems = items,
                        totalAmount = items.sumOf { item -> item.product.price * item.quantity }
                    )
                }
            }
        }
    }

    override fun processEvent(event: Event) {
        when (event) {
            is Event.OnFullNameChanged -> updateState { it.copy(fullName = event.value) }
            is Event.OnAddressChanged -> updateState { it.copy(address = event.value) }
            is Event.OnCityChanged -> updateState { it.copy(city = event.value) }
            is Event.OnPostalCodeChanged -> updateState { it.copy(postalCode = event.value) }
            is Event.OnPhoneNumberChanged -> updateState { it.copy(phoneNumber = event.value) }
            Event.OnPlaceOrder -> onPlaceOrderClick()
            Event.OnNavigateBack -> sendEffect(Effect.NavigateBack)
            Event.OnShowPinVerification -> updateState { it.copy(isPinVerificationVisible = true, pinError = null) }
            Event.OnHidePinVerification -> updateState { it.copy(isPinVerificationVisible = false, pinError = null) }
            is Event.OnPinEntered -> verifyPinAndPlaceOrder(event.pin)
        }
    }

    private fun onPlaceOrderClick() {
        val currentState = state.value
        if (validateInput(currentState)) {
            processEvent(Event.OnShowPinVerification)
        } else {
            sendEffect(Effect.ShowToast("Please fill all fields"))
        }
    }

    private fun verifyPinAndPlaceOrder(pin: String) {
        // Mock PIN verification
        if (pin == "123456") {
            updateState { it.copy(isPinVerificationVisible = false) }
            placeOrder()
        } else {
            updateState { it.copy(pinError = "Invalid PIN") }
        }
    }

    private fun placeOrder() {
        val currentState = state.value
        viewModelScope.launch {
            updateState { it.copy(placeOrderState = ResultState.Loading) }
            try {
                val shippingAddress = ShippingAddress(
                    fullName = currentState.fullName,
                    addressLine = currentState.address,
                    city = currentState.city,
                    postalCode = currentState.postalCode,
                    phoneNumber = currentState.phoneNumber
                )
                orderRepository.placeOrder(
                    items = currentState.cartItems,
                    shippingAddress = shippingAddress,
                    totalAmount = currentState.totalAmount
                )
                addressRepository.saveLastUsedAddress(shippingAddress)
                updateState { it.copy(placeOrderState = ResultState.Success(Unit)) }
                sendEffect(Effect.NavigateToTransactionDetail(true, "Order placed successfully!"))
            } catch (e: Exception) {
                updateState { it.copy(placeOrderState = ResultState.Error(e)) }
                sendEffect(Effect.NavigateToTransactionDetail(false, "Failed to place order: ${e.message}"))
            }
        }
    }

    private fun validateInput(state: State): Boolean {
        return state.fullName.isNotBlank() &&
                state.address.isNotBlank() &&
                state.city.isNotBlank() &&
                state.postalCode.isNotBlank() &&
                state.phoneNumber.isNotBlank()
    }

    data class State(
        val cartItems: List<CartItemModel> = emptyList(),
        val totalAmount: Double = 0.0,
        val fullName: String = "",
        val address: String = "",
        val city: String = "",
        val postalCode: String = "",
        val phoneNumber: String = "",
        val placeOrderState: ResultState<Unit> = ResultState.Idle,
        val isPinVerificationVisible: Boolean = false,
        val pinError: String? = null
    )

    sealed interface Event {
        data class OnFullNameChanged(val value: String) : Event
        data class OnAddressChanged(val value: String) : Event
        data class OnCityChanged(val value: String) : Event
        data class OnPostalCodeChanged(val value: String) : Event
        data class OnPhoneNumberChanged(val value: String) : Event
        data object OnPlaceOrder : Event
        data object OnNavigateBack : Event
        data object OnShowPinVerification : Event
        data object OnHidePinVerification : Event
        data class OnPinEntered(val pin: String) : Event
    }

    sealed interface Effect {
        data class ShowToast(val message: String) : Effect
        data object NavigateBack : Effect
        data class NavigateToTransactionDetail(val isSuccess: Boolean, val message: String) : Effect
    }
}


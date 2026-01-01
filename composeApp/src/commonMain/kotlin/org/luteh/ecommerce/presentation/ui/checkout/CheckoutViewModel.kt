package org.luteh.ecommerce.presentation.ui.checkout

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.model.ShippingAddress
import org.luteh.ecommerce.domain.repository.CartRepository
import org.luteh.ecommerce.domain.repository.OrderRepository
import org.luteh.ecommerce.presentation.core.BaseViewModel
import org.luteh.ecommerce.presentation.core.ResultState

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : BaseViewModel<CheckoutViewModel.State, CheckoutViewModel.Event, CheckoutViewModel.Effect>(State()) {

    init {
        loadCartItems()
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
            Event.OnPlaceOrder -> placeOrder()
            Event.OnNavigateBack -> sendEffect(Effect.NavigateBack)
        }
    }

    private fun placeOrder() {
        val currentState = state.value
        if (validateInput(currentState)) {
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
                    updateState { it.copy(placeOrderState = ResultState.Success(Unit)) }
                    sendEffect(Effect.OrderPlacedSuccess)
                } catch (e: Exception) {
                    updateState { it.copy(placeOrderState = ResultState.Error(e)) }
                    sendEffect(Effect.ShowToast("Failed to place order: ${e.message}"))
                }
            }
        } else {
            sendEffect(Effect.ShowToast("Please fill all fields"))
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
        val placeOrderState: ResultState<Unit> = ResultState.Idle
    )

    sealed interface Event {
        data class OnFullNameChanged(val value: String) : Event
        data class OnAddressChanged(val value: String) : Event
        data class OnCityChanged(val value: String) : Event
        data class OnPostalCodeChanged(val value: String) : Event
        data class OnPhoneNumberChanged(val value: String) : Event
        data object OnPlaceOrder : Event
        data object OnNavigateBack : Event
    }

    sealed interface Effect {
        data class ShowToast(val message: String) : Effect
        data object NavigateBack : Effect
        data object OrderPlacedSuccess : Effect
    }
}


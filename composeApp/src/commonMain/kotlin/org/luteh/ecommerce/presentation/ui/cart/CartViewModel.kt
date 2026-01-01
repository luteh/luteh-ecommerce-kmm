package org.luteh.ecommerce.presentation.ui.cart

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.luteh.ecommerce.domain.model.CartItemModel
import org.luteh.ecommerce.domain.repository.CartRepository
import org.luteh.ecommerce.domain.usecase.cart.UpdateCartItemQuantityUseCase
import org.luteh.ecommerce.presentation.core.BaseViewModel
import org.luteh.ecommerce.presentation.core.ResultState

class CartViewModel(
    private val cartRepository: CartRepository,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase
) : BaseViewModel<CartViewModel.State, CartViewModel.Event, CartViewModel.Effect>(State()) {

    init {
        getCartItems()
    }

    private fun getCartItems() {
        viewModelScope.launch {
            updateState { it.copy(cartItemsState = ResultState.Loading) }
            try {
                cartRepository.getCartItems().collectLatest { items ->
                    updateState {
                        it.copy(
                            cartItemsState = ResultState.Success(items),
                            totalPrice = items.sumOf { item -> item.product.price * item.quantity }
                        )
                    }
                }
            } catch (e: Exception) {
                updateState { it.copy(cartItemsState = ResultState.Error(e)) }
            }
        }
    }

    override fun processEvent(event: Event) {
        when (event) {
            is Event.OnUpdateQuantity -> updateQuantity(event.productId, event.quantity)
            is Event.OnRemoveItem -> removeItem(event.productId)
            Event.OnClearCart -> clearCart()
            Event.OnCheckout -> checkout()
            Event.OnNavigateBack -> sendEffect(Effect.NavigateBack)
        }
    }

    private fun updateQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            try {
                updateCartItemQuantityUseCase(productId, quantity)
            } catch (e: Exception) {
                sendEffect(Effect.ShowToast("Failed to update quantity: ${e.message}"))
            }
        }
    }

    private fun removeItem(productId: String) {
        viewModelScope.launch {
            try {
                cartRepository.removeFromCart(productId)
                sendEffect(Effect.ShowToast("Item removed"))
            } catch (e: Exception) {
                sendEffect(Effect.ShowToast("Failed to remove item: ${e.message}"))
            }
        }
    }

    private fun clearCart() {
        viewModelScope.launch {
            try {
                cartRepository.clearCart()
                sendEffect(Effect.ShowToast("Cart cleared"))
            } catch (e: Exception) {
                sendEffect(Effect.ShowToast("Failed to clear cart: ${e.message}"))
            }
        }
    }

    private fun checkout() {
        sendEffect(Effect.NavigateToCheckout)
    }

    data class State(
        val cartItemsState: ResultState<List<CartItemModel>> = ResultState.Idle,
        val totalPrice: Double = 0.0
    )

    sealed interface Event {
        data class OnUpdateQuantity(val productId: String, val quantity: Int) : Event
        data class OnRemoveItem(val productId: String) : Event
        data object OnClearCart : Event
        data object OnCheckout : Event
        data object OnNavigateBack : Event
    }

    sealed interface Effect {
        data class ShowToast(val message: String) : Effect
        data object NavigateBack : Effect
        data object NavigateToCheckout : Effect
    }
}


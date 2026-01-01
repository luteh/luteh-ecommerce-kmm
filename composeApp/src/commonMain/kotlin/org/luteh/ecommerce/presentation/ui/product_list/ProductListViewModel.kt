package org.luteh.ecommerce.presentation.ui.product_list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.luteh.ecommerce.domain.usecase.auth.CheckSessionUseCase
import org.luteh.ecommerce.presentation.core.BaseViewModel

class ProductListViewModel(private val checkSessionUseCase: CheckSessionUseCase) :
    BaseViewModel<
        ProductListViewModel.State,
        ProductListViewModel.Event,
        ProductListViewModel.Effect,
    >(State()) {

    data class State(val isLoading: Boolean = false)

    sealed interface Event {
        data object OnCartClicked : Event
    }

    sealed interface Effect {
        data object NavigateToCart : Effect

        data object NavigateToLogin : Effect
    }

    override fun processEvent(event: Event) {
        when (event) {
            Event.OnCartClicked -> checkAuthAndNavigateToCart()
        }
    }

    private fun checkAuthAndNavigateToCart() {
        viewModelScope.launch {
            val isLoggedIn = checkSessionUseCase()
            if (isLoggedIn) {
                sendEffect(Effect.NavigateToCart)
            } else {
                sendEffect(Effect.NavigateToLogin)
            }
        }
    }
}

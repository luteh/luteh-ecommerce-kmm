package org.luteh.ecommerce.presentation.ui.transaction_detail

import org.luteh.ecommerce.presentation.core.BaseViewModel

class TransactionDetailViewModel : BaseViewModel<TransactionDetailViewModel.State, TransactionDetailViewModel.Event, TransactionDetailViewModel.Effect>(State()) {

    data class State(
        val isSuccess: Boolean = false,
        val message: String = ""
    )

    sealed interface Event {
        data class Init(val isSuccess: Boolean, val message: String) : Event
        data object OnBackToHome : Event
    }

    sealed interface Effect {
        data object NavigateToHome : Effect
    }

    override fun processEvent(event: Event) {
        when (event) {
            is Event.Init -> updateState { it.copy(isSuccess = event.isSuccess, message = event.message) }
            Event.OnBackToHome -> sendEffect(Effect.NavigateToHome)
        }
    }
}


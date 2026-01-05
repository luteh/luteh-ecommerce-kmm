package org.luteh.ecommerce.presentation.ui.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.presentation.core.BaseViewModel

class HomeViewModel(private val authRepository: AuthRepository) :
    BaseViewModel<HomeViewModel.State, HomeViewModel.Event, HomeViewModel.Effect>(State()) {

    data class State(val isLoggedIn: Boolean = false)

    sealed interface Event

    sealed interface Effect

    init {
        observeSession()
    }

    override fun processEvent(event: Event) {
        // No events to process
    }

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.observeLoginSession().collect { isLoggedIn ->
                updateState { it.copy(isLoggedIn = isLoggedIn) }
            }
        }
    }
}

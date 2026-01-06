package org.luteh.ecommerce.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.luteh.ecommerce.domain.repository.AuthRepository

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        observeSessionState()
    }

    private fun observeSessionState() {
        viewModelScope.launch {
            authRepository.observeLoginSession().collect { isLoggedIn ->
                _state.update { it.copy(isLoggedIn = isLoggedIn) }
            }
        }
    }

    data class State(val isLoggedIn: Boolean = false)
}

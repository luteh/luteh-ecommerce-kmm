package org.luteh.ecommerce.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.domain.usecase.auth.LogoutUseCase

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadUserProfile()
    }

    fun processEvent(event: Event) {
        when (event) {
            is Event.OnLogoutClick -> showLogoutConfirmation()
            is Event.OnConfirmLogout -> performLogout()
            is Event.OnDismissLogoutDialog -> dismissLogoutDialog()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val email = authRepository.getAccount()
            val name = extractNameFromEmail(email)
            val initials = extractInitials(name)

            _state.update { it.copy(userName = name, userEmail = email, userInitials = initials) }
        }
    }

    private fun showLogoutConfirmation() {
        _state.update { it.copy(showLogoutDialog = true) }
    }

    private fun dismissLogoutDialog() {
        _state.update { it.copy(showLogoutDialog = false) }
    }

    private fun performLogout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoggingOut = true, showLogoutDialog = false) }

            try {
                logoutUseCase()
                _state.update { it.copy(isLoggingOut = false) }
            } catch (_: Exception) {
                _state.update { it.copy(isLoggingOut = false) }
                _effect.send(Effect.ShowError("Failed to logout. Please try again."))
            }
        }
    }

    private fun extractNameFromEmail(email: String): String {
        if (email.isEmpty()) return "User"
        val namePart = email.substringBefore("@")
        return namePart.split(".", "_", "-").joinToString(" ") {
            it.replaceFirstChar { char -> char.uppercase() }
        }
    }

    private fun extractInitials(name: String): String {
        val parts = name.split(" ")
        return when {
            parts.size >= 2 ->
                "${parts[0].firstOrNull()?.uppercase() ?: ""}${parts[1].firstOrNull()?.uppercase() ?: ""}"
            parts.isNotEmpty() -> parts[0].take(2).uppercase()
            else -> "U"
        }
    }

    data class State(
        val userName: String = "",
        val userEmail: String = "",
        val userInitials: String = "",
        val showLogoutDialog: Boolean = false,
        val isLoggingOut: Boolean = false,
    )

    sealed interface Event {
        data object OnLogoutClick : Event

        data object OnConfirmLogout : Event

        data object OnDismissLogoutDialog : Event
    }

    sealed interface Effect {
        data class ShowError(val message: String) : Effect
    }
}

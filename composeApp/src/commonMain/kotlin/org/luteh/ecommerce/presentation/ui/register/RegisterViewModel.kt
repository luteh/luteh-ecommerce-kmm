package org.luteh.ecommerce.presentation.ui.register

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.luteh.ecommerce.domain.model.RegisterParam
import org.luteh.ecommerce.domain.model.UserRole
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.presentation.core.BaseViewModel
import org.luteh.ecommerce.presentation.core.ResultState

class RegisterViewModel(private val authRepository: AuthRepository) :
    BaseViewModel<RegisterViewModel.State, RegisterViewModel.Event, RegisterViewModel.Effect>(State()) {

    init {
        getUserRoles()
    }

    override fun processEvent(event: Event) {
        when (event) {
            is Event.OnChangeEmailText -> changeEmailText(event.value)
            is Event.OnChangeNameText -> changeNameText(event.value)
            is Event.OnChangePasswordText -> changePasswordText(event.value)
            is Event.OnChangePhoneText -> changePhoneText(event.value)
            Event.OnClickRegisterButton -> submitRegistration()
            is Event.OnSelectRoleOption -> changeSelectedRole(event.value)
            Event.OnClickRefreshButton -> getUserRoles()
            Event.OnClickBackButton -> sendEffect(Effect.NavigateBack)
        }
    }

    private fun submitRegistration() {
        viewModelScope.launch {
            updateState { it.copy(registerResult = ResultState.Loading) }
            authRepository.register(
                RegisterParam(
                    email = state.value.email,
                    password = state.value.password,
                    name = state.value.name,
                    phone = state.value.phone,
                    roleId = state.value.selectedRole!!.id
                )
            ).fold(
                { exception ->
                    updateState { it.copy(registerResult = ResultState.Error(exception)) }
                    sendEffect(Effect.ShowToast(exception.message ?: "Something went wrong"))
                },
                {
                    updateState { it.copy(registerResult = ResultState.Success(Unit)) }
                    sendEffect(Effect.ShowToast("Registration successful"))
                    sendEffect(Effect.NavigateBack)
                }

            )
        }
    }

    private fun getUserRoles() {
        viewModelScope.launch {
            updateState { it.copy(getRolesResult = ResultState.Loading) }
            authRepository.getUserRoles().fold(
                { exception ->
                    updateState { it.copy(getRolesResult = ResultState.Error(exception)) }
                    sendEffect(Effect.ShowToast(exception.message ?: "Something went wrong"))
                },
                { data ->
                    updateState { it.copy(getRolesResult = ResultState.Success(data)) }
                }
            )
        }
    }

    private fun changeEmailText(value: String) {
        updateState { it.copy(email = value) }
        validateForm()
    }

    private fun changePasswordText(value: String) {
        updateState { it.copy(password = value) }
        validateForm()
    }

    private fun changeNameText(value: String) {
        updateState { it.copy(name = value) }
        validateForm()
    }

    private fun changePhoneText(value: String) {
        updateState { it.copy(phone = value) }
        validateForm()
    }

    private fun changeSelectedRole(value: UserRole) {
        updateState { it.copy(selectedRole = value) }
        validateForm()
    }

    private fun validateForm() {
        updateState {
            it.copy(
                enableRegisterButton = it.email.isNotBlank() &&
                        it.password.isNotBlank() &&
                        it.name.isNotBlank() &&
                        it.phone.isNotBlank() &&
                        it.selectedRole != null
            )
        }
    }

    data class State(
        val registerResult: ResultState<Unit> = ResultState.Idle,
        val getRolesResult: ResultState<List<UserRole>> = ResultState.Idle,
        val email: String = "",
        val password: String = "",
        val name: String = "",
        val phone: String = "",
        val selectedRole: UserRole? = null,
        val enableRegisterButton: Boolean = false
    )

    sealed interface Event {
        data class OnChangeEmailText(val value: String) : Event
        data class OnChangePasswordText(val value: String) : Event
        data class OnChangeNameText(val value: String) : Event
        data class OnChangePhoneText(val value: String) : Event
        data class OnSelectRoleOption(val value: UserRole) : Event
        data object OnClickRegisterButton : Event
        data object OnClickRefreshButton : Event
        data object OnClickBackButton : Event
    }

    sealed interface Effect {
        data class ShowToast(val message: String) : Effect
        data object NavigateBack : Effect
    }
}
package org.luteh.ecommerce.presentation.ui.login

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.luteh.ecommerce.domain.usecase.auth.LoginUseCase
import org.luteh.ecommerce.presentation.core.BaseViewModel
import org.luteh.ecommerce.presentation.core.ResultState

class LoginViewModel(private val loginUseCase: LoginUseCase) :
    BaseViewModel<LoginViewModel.State, LoginViewModel.Event, LoginViewModel.Effect>(State()) {

    init {
        //        checkLoginSession()
    }

    override fun processEvent(event: Event) {
        when (event) {
            Event.OnClickGoogleSignInButton -> launchGoogleSignIn()
            is Event.OnChangePasswordText -> changePasswordText(event.value)
            is Event.OnChangeEmailText -> changeEmailText(event.value)
            Event.OnClickLoginButton -> doLogin()
            Event.OnClickRegisterButton -> navigateToRegisterScreen()
        }
    }

    private fun navigateToRegisterScreen() {
        sendEffect(Effect.NavigateToRegisterScreen)
    }

    private fun launchGoogleSignIn() {
        sendEffect(Effect.ShowToast("Launch Google Sign In"))
    }

    private fun doLogin() {
        viewModelScope.launch {
            updateState { it.copy(loginState = ResultState.Loading) }

            loginUseCase(email = state.value.email, password = state.value.password)
                .fold(
                    { exception ->
                        updateState { it.copy(loginState = ResultState.Error(exception)) }
                        sendEffect(Effect.ShowToast(exception.message.toString()))
                    },
                    {
                        updateState { it.copy(loginState = ResultState.Success(Unit)) }
                        sendEffect(Effect.NavigateBack)
                    },
                )
        }
    }

    private fun changePasswordText(value: String) {
        updateState { it.copy(password = value) }
    }

    private fun changeEmailText(value: String) {
        updateState { it.copy(email = value) }
    }

    data class State(
        val loginState: ResultState<Unit> = ResultState.Idle,
        val email: String = "",
        val password: String = "",
    )

    sealed interface Event {
        data class OnChangeEmailText(val value: String) : Event

        data class OnChangePasswordText(val value: String) : Event

        data object OnClickGoogleSignInButton : Event

        data object OnClickLoginButton : Event

        data object OnClickRegisterButton : Event
    }

    sealed interface Effect {
        data class ShowToast(val message: String) : Effect

        data object NavigateToMainScreen : Effect
        data object NavigateBack : Effect

        data object NavigateToRegisterScreen : Effect
    }
}

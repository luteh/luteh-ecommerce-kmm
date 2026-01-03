package org.luteh.ecommerce.presentation.ui.login

import app.cash.turbine.test
import arrow.core.Either
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.domain.usecase.auth.LoginUseCase
import org.luteh.ecommerce.presentation.core.ResultState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var loginUseCase: LoginUseCase

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mock(MockMode.autoUnit)
        loginUseCase = LoginUseCase(authRepository)
        viewModel = LoginViewModel(loginUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `OnChangeEmailText should update email state`() = runTest {
        viewModel.processEvent(LoginViewModel.Event.OnChangeEmailText("test@example.com"))
        assertEquals("test@example.com", viewModel.state.value.email)
    }

    @Test
    fun `OnChangePasswordText should update password state`() = runTest {
        viewModel.processEvent(LoginViewModel.Event.OnChangePasswordText("password123"))
        assertEquals("password123", viewModel.state.value.password)
    }

    @Test
    fun `OnClickRegisterButton should navigate to register screen`() = runTest {
        viewModel.effect.test {
            viewModel.processEvent(LoginViewModel.Event.OnClickRegisterButton)
            assertEquals(LoginViewModel.Effect.NavigateToRegisterScreen, awaitItem())
        }
    }

    @Test
    fun `OnClickGoogleSignInButton should show toast`() = runTest {
        viewModel.effect.test {
            viewModel.processEvent(LoginViewModel.Event.OnClickGoogleSignInButton)
            val effect = awaitItem()
            assertTrue(effect is LoginViewModel.Effect.ShowToast)
            assertEquals("Launch Google Sign In", effect.message)
        }
    }

    @Test
    fun `OnClickLoginButton success should update state and navigate back`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password"
        everySuspend { authRepository.login(email, password) } returns Either.Right(Unit)

        viewModel.processEvent(LoginViewModel.Event.OnChangeEmailText(email))
        viewModel.processEvent(LoginViewModel.Event.OnChangePasswordText(password))

        viewModel.effect.test {
            viewModel.state.test {
                // Initial state
                val initialState = awaitItem()
                assertTrue(initialState.loginState is ResultState.Idle)

                // Act
                viewModel.processEvent(LoginViewModel.Event.OnClickLoginButton)

                // Loading state
                val loadingState = awaitItem()
                assertTrue(loadingState.loginState is ResultState.Loading)

                // Success state
                val successState = awaitItem()
                assertTrue(successState.loginState is ResultState.Success)
            }

            assertEquals(LoginViewModel.Effect.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `OnClickLoginButton failure should update state and show toast`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "wrong_password"
        val error = Exception("Login failed")
        everySuspend { authRepository.login(email, password) } returns Either.Left(error)

        viewModel.processEvent(LoginViewModel.Event.OnChangeEmailText(email))
        viewModel.processEvent(LoginViewModel.Event.OnChangePasswordText(password))

        viewModel.effect.test {
            viewModel.state.test {
                // Initial state
                val initialState = awaitItem()
                assertTrue(initialState.loginState is ResultState.Idle)

                // Act
                viewModel.processEvent(LoginViewModel.Event.OnClickLoginButton)

                // Loading state
                val loadingState = awaitItem()
                assertTrue(loadingState.loginState is ResultState.Loading)

                // Error state
                val errorState = awaitItem()
                assertTrue(errorState.loginState is ResultState.Error)
                assertEquals(error, errorState.loginState.exception)
            }

            val effect = awaitItem()
            assertTrue(effect is LoginViewModel.Effect.ShowToast)
            assertEquals("Login failed", effect.message)
        }
    }
}

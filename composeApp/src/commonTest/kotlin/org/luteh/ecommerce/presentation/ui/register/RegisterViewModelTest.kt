package org.luteh.ecommerce.presentation.ui.register

import app.cash.turbine.test
import arrow.core.Either
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.luteh.ecommerce.domain.model.RegisterParam
import org.luteh.ecommerce.domain.model.UserRole
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.presentation.core.ResultState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mock(MockMode.autoUnit)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = RegisterViewModel(authRepository)
    }

    @Test
    fun `init should load user roles and update state`() = runTest {
        val roles = listOf(UserRole("1", "Admin"), UserRole("2", "User"))
        everySuspend { authRepository.getUserRoles() } returns Either.Right(roles)

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.getRolesResult is ResultState.Success)
        assertEquals(roles, state.getRolesResult.data)
    }

    @Test
    fun `init should handle error when loading user roles fails`() = runTest {
        val exception = RuntimeException("Network error")
        everySuspend { authRepository.getUserRoles() } returns Either.Left(exception)

        createViewModel()
        viewModel.effect.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assertTrue(state.getRolesResult is ResultState.Error)
            assertEquals(exception, state.getRolesResult.exception)

            val effect = awaitItem()
            assertTrue(effect is RegisterViewModel.Effect.ShowToast)
            assertEquals("Network error", effect.message)
        }
    }

    @Test
    fun `OnChangeEmailText should update email and validate form`() = runTest {
        everySuspend { authRepository.getUserRoles() } returns Either.Right(emptyList())
        createViewModel()

        viewModel.processEvent(RegisterViewModel.Event.OnChangeEmailText("test@example.com"))

        val state = viewModel.state.value
        assertEquals("test@example.com", state.email)
        assertFalse(state.enableRegisterButton) // Other fields are empty
    }

    @Test
    fun `OnChangePasswordText should update password and validate form`() = runTest {
        everySuspend { authRepository.getUserRoles() } returns Either.Right(emptyList())
        createViewModel()

        viewModel.processEvent(RegisterViewModel.Event.OnChangePasswordText("password123"))

        val state = viewModel.state.value
        assertEquals("password123", state.password)
        assertFalse(state.enableRegisterButton)
    }

    @Test
    fun `OnChangeNameText should update name and validate form`() = runTest {
        everySuspend { authRepository.getUserRoles() } returns Either.Right(emptyList())
        createViewModel()

        viewModel.processEvent(RegisterViewModel.Event.OnChangeNameText("John Doe"))

        val state = viewModel.state.value
        assertEquals("John Doe", state.name)
        assertFalse(state.enableRegisterButton)
    }

    @Test
    fun `OnChangePhoneText should update phone and validate form`() = runTest {
        everySuspend { authRepository.getUserRoles() } returns Either.Right(emptyList())
        createViewModel()

        viewModel.processEvent(RegisterViewModel.Event.OnChangePhoneText("1234567890"))

        val state = viewModel.state.value
        assertEquals("1234567890", state.phone)
        assertFalse(state.enableRegisterButton)
    }

    @Test
    fun `OnSelectRoleOption should update selected role and validate form`() = runTest {
        everySuspend { authRepository.getUserRoles() } returns Either.Right(emptyList())
        createViewModel()
        val role = UserRole("1", "Admin")

        viewModel.processEvent(RegisterViewModel.Event.OnSelectRoleOption(role))

        val state = viewModel.state.value
        assertEquals(role, state.selectedRole)
        assertFalse(state.enableRegisterButton)
    }

    @Test
    fun `Form should be enabled when all fields are valid`() = runTest {
        everySuspend { authRepository.getUserRoles() } returns Either.Right(emptyList())
        createViewModel()
        val role = UserRole("1", "Admin")

        viewModel.processEvent(RegisterViewModel.Event.OnChangeEmailText("test@example.com"))
        viewModel.processEvent(RegisterViewModel.Event.OnChangePasswordText("password123"))
        viewModel.processEvent(RegisterViewModel.Event.OnChangeNameText("John Doe"))
        viewModel.processEvent(RegisterViewModel.Event.OnChangePhoneText("1234567890"))
        viewModel.processEvent(RegisterViewModel.Event.OnSelectRoleOption(role))

        assertTrue(viewModel.state.value.enableRegisterButton)
    }

    @Test
    fun `OnClickRegisterButton success should call repository and navigate back`() = runTest {
        everySuspend { authRepository.getUserRoles() } returns Either.Right(emptyList())
        everySuspend { authRepository.register(any()) } returns Either.Right(Unit)
        createViewModel()
        val role = UserRole("1", "Admin")

        // Fill form
        viewModel.processEvent(RegisterViewModel.Event.OnChangeEmailText("test@example.com"))
        viewModel.processEvent(RegisterViewModel.Event.OnChangePasswordText("password123"))
        viewModel.processEvent(RegisterViewModel.Event.OnChangeNameText("John Doe"))
        viewModel.processEvent(RegisterViewModel.Event.OnChangePhoneText("1234567890"))
        viewModel.processEvent(RegisterViewModel.Event.OnSelectRoleOption(role))

        viewModel.effect.test {
            viewModel.processEvent(RegisterViewModel.Event.OnClickRegisterButton)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assertTrue(state.registerResult is ResultState.Success)

            val toastEffect = awaitItem()
            assertTrue(toastEffect is RegisterViewModel.Effect.ShowToast)
            assertEquals("Registration successful", toastEffect.message)

            val navEffect = awaitItem()
            assertEquals(RegisterViewModel.Effect.NavigateBack, navEffect)

            verifySuspend {
                authRepository.register(
                    RegisterParam(
                        email = "test@example.com",
                        password = "password123",
                        name = "John Doe",
                        phone = "1234567890",
                        roleId = "1",
                    )
                )
            }
        }
    }

    @Test
    fun `OnClickRegisterButton failure should show toast`() = runTest {
        everySuspend { authRepository.getUserRoles() } returns Either.Right(emptyList())
        val exception = RuntimeException("Registration failed")
        everySuspend { authRepository.register(any()) } returns Either.Left(exception)
        createViewModel()
        val role = UserRole("1", "Admin")

        // Fill form
        viewModel.processEvent(RegisterViewModel.Event.OnChangeEmailText("test@example.com"))
        viewModel.processEvent(RegisterViewModel.Event.OnChangePasswordText("password123"))
        viewModel.processEvent(RegisterViewModel.Event.OnChangeNameText("John Doe"))
        viewModel.processEvent(RegisterViewModel.Event.OnChangePhoneText("1234567890"))
        viewModel.processEvent(RegisterViewModel.Event.OnSelectRoleOption(role))

        viewModel.effect.test {
            viewModel.processEvent(RegisterViewModel.Event.OnClickRegisterButton)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assertTrue(state.registerResult is ResultState.Error)
            assertEquals(exception, state.registerResult.exception)

            val effect = awaitItem()
            assertTrue(effect is RegisterViewModel.Effect.ShowToast)
            assertEquals("Registration failed", effect.message)
        }
    }

    @Test
    fun `OnClickRefreshButton should reload user roles`() = runTest {
        val roles = listOf(UserRole("1", "Admin"))
        everySuspend { authRepository.getUserRoles() } returns Either.Right(roles)
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Simulate refresh
        val newRoles = listOf(UserRole("1", "Admin"), UserRole("2", "User"))
        everySuspend { authRepository.getUserRoles() } returns Either.Right(newRoles)

        viewModel.processEvent(RegisterViewModel.Event.OnClickRefreshButton)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.getRolesResult is ResultState.Success)
        assertEquals(newRoles, state.getRolesResult.data)
    }

    @Test
    fun `OnClickBackButton should navigate back`() = runTest {
        everySuspend { authRepository.getUserRoles() } returns Either.Right(emptyList())
        createViewModel()

        viewModel.effect.test {
            viewModel.processEvent(RegisterViewModel.Event.OnClickBackButton)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(RegisterViewModel.Effect.NavigateBack, awaitItem())
        }
    }
}

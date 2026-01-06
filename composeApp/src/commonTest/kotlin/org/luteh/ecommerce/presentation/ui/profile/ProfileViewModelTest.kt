package org.luteh.ecommerce.presentation.ui.profile

import app.cash.turbine.test
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.domain.usecase.auth.LogoutUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var logoutUseCase: LogoutUseCase

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
        logoutUseCase = LogoutUseCase(authRepository)
        viewModel = ProfileViewModel(authRepository, logoutUseCase)
    }

    @Test
    fun `init should load user profile from repository`() = runTest {
        everySuspend { authRepository.getAccount() } returns "john.doe@email.com"

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("John Doe", state.userName)
        assertEquals("john.doe@email.com", state.userEmail)
        assertEquals("JD", state.userInitials)
    }

    @Test
    fun `init should handle empty email gracefully`() = runTest {
        everySuspend { authRepository.getAccount() } returns ""

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("User", state.userName)
        assertEquals("", state.userEmail)
        assertEquals("US", state.userInitials)
    }

    @Test
    fun `init should extract name from email with underscore separator`() = runTest {
        everySuspend { authRepository.getAccount() } returns "jane_smith@email.com"

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Jane Smith", state.userName)
        assertEquals("JS", state.userInitials)
    }

    @Test
    fun `init should extract name from email with dash separator`() = runTest {
        everySuspend { authRepository.getAccount() } returns "bob-jones@email.com"

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Bob Jones", state.userName)
        assertEquals("BJ", state.userInitials)
    }

    @Test
    fun `init should handle single word email username`() = runTest {
        everySuspend { authRepository.getAccount() } returns "admin@email.com"

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Admin", state.userName)
        assertEquals("AD", state.userInitials)
    }

    @Test
    fun `OnLogoutClick should show logout confirmation dialog`() = runTest {
        everySuspend { authRepository.getAccount() } returns "test@email.com"

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processEvent(ProfileViewModel.Event.OnLogoutClick)

        assertTrue(viewModel.state.value.showLogoutDialog)
    }

    @Test
    fun `OnDismissLogoutDialog should hide logout dialog`() = runTest {
        everySuspend { authRepository.getAccount() } returns "test@email.com"

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processEvent(ProfileViewModel.Event.OnLogoutClick)
        assertTrue(viewModel.state.value.showLogoutDialog)

        viewModel.processEvent(ProfileViewModel.Event.OnDismissLogoutDialog)
        assertFalse(viewModel.state.value.showLogoutDialog)
    }

    @Test
    fun `OnConfirmLogout should call logout and update state`() = runTest {
        everySuspend { authRepository.getAccount() } returns "test@email.com"
        everySuspend { authRepository.logout() } returns Unit

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processEvent(ProfileViewModel.Event.OnLogoutClick)
        viewModel.processEvent(ProfileViewModel.Event.OnConfirmLogout)
        testDispatcher.scheduler.advanceUntilIdle()

        verifySuspend { authRepository.logout() }
        assertFalse(viewModel.state.value.showLogoutDialog)
        assertFalse(viewModel.state.value.isLoggingOut)
    }

    @Test
    fun `OnConfirmLogout should hide dialog immediately`() = runTest {
        everySuspend { authRepository.getAccount() } returns "test@email.com"
        everySuspend { authRepository.logout() } returns Unit

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processEvent(ProfileViewModel.Event.OnLogoutClick)
        assertTrue(viewModel.state.value.showLogoutDialog)

        viewModel.processEvent(ProfileViewModel.Event.OnConfirmLogout)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.showLogoutDialog)
    }

    @Test
    fun `OnConfirmLogout failure should show error and reset loading state`() = runTest {
        everySuspend { authRepository.getAccount() } returns "test@email.com"
        everySuspend { authRepository.logout() } throws RuntimeException("Logout failed")

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            viewModel.processEvent(ProfileViewModel.Event.OnLogoutClick)
            viewModel.processEvent(ProfileViewModel.Event.OnConfirmLogout)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is ProfileViewModel.Effect.ShowError)
            assertEquals("Failed to logout. Please try again.", effect.message)
        }

        assertFalse(viewModel.state.value.isLoggingOut)
    }

    @Test
    fun `initial state should have default values`() = runTest {
        everySuspend { authRepository.getAccount() } returns ""

        createViewModel()

        val state = viewModel.state.value
        assertEquals("", state.userName)
        assertEquals("", state.userEmail)
        assertEquals("", state.userInitials)
        assertFalse(state.showLogoutDialog)
        assertFalse(state.isLoggingOut)
    }

    @Test
    fun `logout dialog should be dismissed when confirming logout`() = runTest {
        everySuspend { authRepository.getAccount() } returns "test@email.com"
        everySuspend { authRepository.logout() } returns Unit

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.processEvent(ProfileViewModel.Event.OnLogoutClick)
        assertTrue(viewModel.state.value.showLogoutDialog)

        viewModel.processEvent(ProfileViewModel.Event.OnConfirmLogout)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.showLogoutDialog)
    }
}

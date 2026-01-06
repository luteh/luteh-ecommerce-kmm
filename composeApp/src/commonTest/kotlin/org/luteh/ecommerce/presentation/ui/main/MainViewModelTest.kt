package org.luteh.ecommerce.presentation.ui.main

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.luteh.ecommerce.domain.repository.AuthRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
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
        viewModel = MainViewModel(authRepository)
    }

    @Test
    fun `init should observe login session and update state when logged in`() = runTest {
        every { authRepository.observeLoginSession() } returns flowOf(true)

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.isLoggedIn)
    }

    @Test
    fun `init should observe login session and update state when logged out`() = runTest {
        every { authRepository.observeLoginSession() } returns flowOf(false)

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoggedIn)
    }

    @Test
    fun `state should update in realtime when session changes from logged in to logged out`() =
        runTest {
            every { authRepository.observeLoginSession() } returns flowOf(true, false)

            createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            assertFalse(viewModel.state.value.isLoggedIn)
        }

    @Test
    fun `state should update in realtime when session changes from logged out to logged in`() =
        runTest {
            every { authRepository.observeLoginSession() } returns flowOf(false, true)

            createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            assertTrue(viewModel.state.value.isLoggedIn)
        }

    @Test
    fun `initial state should have isLoggedIn as false`() = runTest {
        every { authRepository.observeLoginSession() } returns flowOf()

        createViewModel()

        assertFalse(viewModel.state.value.isLoggedIn)
    }
}

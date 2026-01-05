package org.luteh.ecommerce.presentation.ui.home

import app.cash.turbine.test
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
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
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

    @Test
    fun `init should observe session and update state when logged in`() =
        runTest(testDispatcher) {
            every { authRepository.observeLoginSession() } returns flowOf(true)

            viewModel = HomeViewModel(authRepository)

            viewModel.state.test {
                val firstState = awaitItem()
                if (!firstState.isLoggedIn) {
                    assertEquals(HomeViewModel.State(isLoggedIn = true), awaitItem())
                }
            }
        }

    @Test
    fun `init should observe session and update state when not logged in`() =
        runTest(testDispatcher) {
            every { authRepository.observeLoginSession() } returns flowOf(false)

            viewModel = HomeViewModel(authRepository)

            viewModel.state.test {
                assertEquals(HomeViewModel.State(isLoggedIn = false), awaitItem())
            }
        }

    @Test
    fun `init should update state when session status changes`() =
        runTest(testDispatcher) {
            every { authRepository.observeLoginSession() } returns flowOf(false, true)

            viewModel = HomeViewModel(authRepository)

            viewModel.state.test {
                assertEquals(HomeViewModel.State(isLoggedIn = false), awaitItem())
                assertEquals(HomeViewModel.State(isLoggedIn = true), awaitItem())
            }
        }
}

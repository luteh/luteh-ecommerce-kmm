package org.luteh.ecommerce.presentation.ui.product_list

import app.cash.turbine.test
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
import org.luteh.ecommerce.domain.usecase.auth.CheckSessionUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    private lateinit var viewModel: ProductListViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var checkSessionUseCase: CheckSessionUseCase

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mock(MockMode.autoUnit)
        checkSessionUseCase = CheckSessionUseCase(authRepository)
        viewModel = ProductListViewModel(checkSessionUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `OnCartClicked when logged in should navigate to cart`() = runTest {
        // Arrange
        everySuspend { authRepository.getLoginSession() } returns true

        viewModel.effect.test {
            // Act
            viewModel.processEvent(ProductListViewModel.Event.OnCartClicked)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            assertEquals(ProductListViewModel.Effect.NavigateToCart, awaitItem())
        }
    }

    @Test
    fun `OnCartClicked when not logged in should navigate to login`() = runTest {
        // Arrange
        everySuspend { authRepository.getLoginSession() } returns false

        viewModel.effect.test {
            // Act
            viewModel.processEvent(ProductListViewModel.Event.OnCartClicked)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            assertEquals(ProductListViewModel.Effect.NavigateToLogin, awaitItem())
        }
    }
}

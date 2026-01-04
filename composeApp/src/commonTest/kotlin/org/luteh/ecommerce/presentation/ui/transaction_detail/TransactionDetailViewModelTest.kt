package org.luteh.ecommerce.presentation.ui.transaction_detail

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailViewModelTest {

    private lateinit var viewModel: TransactionDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TransactionDetailViewModel()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Init with success should update state correctly`() = runTest {
        val message = "Transaction Successful"
        viewModel.processEvent(
            TransactionDetailViewModel.Event.Init(isSuccess = true, message = message)
        )

        val state = viewModel.state.value
        assertTrue(state.isSuccess)
        assertEquals(message, state.message)
    }

    @Test
    fun `Init with failure should update state correctly`() = runTest {
        val message = "Transaction Failed"
        viewModel.processEvent(
            TransactionDetailViewModel.Event.Init(isSuccess = false, message = message)
        )

        val state = viewModel.state.value
        assertFalse(state.isSuccess)
        assertEquals(message, state.message)
    }

    @Test
    fun `OnBackToHome should emit NavigateToHome effect`() = runTest {
        viewModel.effect.test {
            viewModel.processEvent(TransactionDetailViewModel.Event.OnBackToHome)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(TransactionDetailViewModel.Effect.NavigateToHome, awaitItem())
        }
    }
}

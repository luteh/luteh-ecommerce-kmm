package org.luteh.ecommerce.domain.usecase.auth

import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.domain.model.SessionResult
import org.luteh.ecommerce.domain.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckSessionUseCaseTest {

    private val authRepository: AuthRepository = mock()
    private val checkSessionUseCase = CheckSessionUseCaseImpl(authRepository)

    @Test
    fun `invoke returns LoggedIn when session is valid`() = runTest {
        everySuspend { authRepository.getLoginSession() } returns true

        val result = checkSessionUseCase()

        assertEquals(SessionResult.LoggedIn, result.getOrNull())
    }

    @Test
    fun `invoke returns NotLoggedIn when session is invalid`() = runTest {
        everySuspend { authRepository.getLoginSession() } returns false

        val result = checkSessionUseCase()

        assertEquals(SessionResult.NotLoggedIn, result.getOrNull())
    }

    @Test
    fun `invoke returns failure when repository throws exception`() = runTest {
        val exception = RuntimeException("Network error")
        everySuspend { authRepository.getLoginSession() } throws exception

        val result = checkSessionUseCase()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}

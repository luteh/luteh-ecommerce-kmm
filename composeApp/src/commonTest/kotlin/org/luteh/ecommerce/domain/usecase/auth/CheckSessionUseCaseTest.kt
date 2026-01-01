package org.luteh.ecommerce.domain.usecase.auth

import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.domain.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckSessionUseCaseTest {

    private val authRepository: AuthRepository = mock()
    private val checkSessionUseCase = CheckSessionUseCase(authRepository)

    @Test
    fun `invoke returns true when session is valid`() = runTest {
        everySuspend { authRepository.getLoginSession() } returns true

        val result = checkSessionUseCase()

        assertTrue(result)
    }

    @Test
    fun `invoke returns false when session is invalid`() = runTest {
        everySuspend { authRepository.getLoginSession() } returns false

        val result = checkSessionUseCase()

        assertFalse(result)
    }
}

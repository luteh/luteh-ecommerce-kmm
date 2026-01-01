package org.luteh.ecommerce.domain.usecase.auth

import arrow.core.Either
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.domain.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginUseCaseTest {

    private val authRepository: AuthRepository = mock()
    private val loginUseCase = LoginUseCase(authRepository)

    @Test
    fun `invoke returns success when login is successful`() = runTest {
        val email = "test@example.com"
        val password = "password"
        everySuspend { authRepository.login(email, password) } returns Either.Right(Unit)

        val result = loginUseCase(email, password)

        assertTrue(result.isRight())
    }

    @Test
    fun `invoke returns error when login fails`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val exception = Exception("Login failed")
        everySuspend { authRepository.login(email, password) } returns Either.Left(exception)

        val result = loginUseCase(email, password)

        assertTrue(result.isLeft())
        assertEquals(exception, result.leftOrNull())
    }
}

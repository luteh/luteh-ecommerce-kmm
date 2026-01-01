package org.luteh.ecommerce.domain.usecase.auth

import dev.mokkery.MockMode
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.domain.repository.AuthRepository
import kotlin.test.Test

class LogoutUseCaseTest {

    private val authRepository: AuthRepository = mock(MockMode.autoUnit)
    private val logoutUseCase = LogoutUseCase(authRepository)

    @Test
    fun `invoke calls logout on repository`() = runTest {
        logoutUseCase()

        verifySuspend { authRepository.logout() }
    }
}

package org.luteh.ecommerce.domain.usecase.auth

import org.luteh.ecommerce.domain.model.SessionResult
import org.luteh.ecommerce.domain.repository.AuthRepository

interface CheckSessionUseCase {
    suspend operator fun invoke(): Result<SessionResult>
}

class CheckSessionUseCaseImpl(private val authRepository: AuthRepository) : CheckSessionUseCase {
    override suspend operator fun invoke(): Result<SessionResult> = runCatching {
        if (authRepository.getLoginSession()) SessionResult.LoggedIn else SessionResult.NotLoggedIn
    }
}

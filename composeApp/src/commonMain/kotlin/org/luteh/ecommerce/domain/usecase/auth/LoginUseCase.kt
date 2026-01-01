package org.luteh.ecommerce.domain.usecase.auth

import arrow.core.Either
import org.luteh.ecommerce.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Either<Exception, Unit> {
        return authRepository.login(email, password)
    }
}

package org.luteh.ecommerce.domain.usecase.auth

import org.luteh.ecommerce.domain.repository.AuthRepository

class CheckSessionUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Boolean {
        return authRepository.getLoginSession()
    }
}

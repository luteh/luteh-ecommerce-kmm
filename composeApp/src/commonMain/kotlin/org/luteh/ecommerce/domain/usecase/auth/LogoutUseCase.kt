package org.luteh.ecommerce.domain.usecase.auth

import org.luteh.ecommerce.domain.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}


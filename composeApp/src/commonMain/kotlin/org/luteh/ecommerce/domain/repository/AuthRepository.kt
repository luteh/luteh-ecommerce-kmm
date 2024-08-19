package org.luteh.ecommerce.domain.repository

import arrow.core.Either
import org.luteh.ecommerce.domain.model.RegisterParam
import org.luteh.ecommerce.domain.model.UserRole

interface AuthRepository {
    suspend fun setLoginSession(isLoggedIn: Boolean)
    suspend fun getLoginSession(): Boolean

    suspend fun getAccount(): String
    suspend fun login(email: String, password: String): Either<Exception, Unit>
    suspend fun getUserRoles(): Either<Exception, List<UserRole>>
    suspend fun register(
        param: RegisterParam
    ): Either<Exception, Unit>
}
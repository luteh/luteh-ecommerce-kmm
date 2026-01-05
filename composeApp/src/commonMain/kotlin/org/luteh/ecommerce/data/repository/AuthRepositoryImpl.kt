package org.luteh.ecommerce.data.repository

import arrow.core.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.luteh.ecommerce.CreateUserMutation
import org.luteh.ecommerce.data.config.FeatureConfig
import org.luteh.ecommerce.data.datasource.remote.AuthRemoteDataSource
import org.luteh.ecommerce.data.local.database.dao.UserSessionDao
import org.luteh.ecommerce.data.local.entity.UserSessionEntity
import org.luteh.ecommerce.domain.model.RegisterParam
import org.luteh.ecommerce.domain.model.UserRole
import org.luteh.ecommerce.domain.repository.AuthRepository
import kotlin.time.Clock

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val featureConfig: FeatureConfig,
    private val userSessionDao: UserSessionDao,
) : AuthRepository {
    override suspend fun setLoginSession(isLoggedIn: Boolean) {}

    override fun observeLoginSession(): Flow<Boolean> {
        return userSessionDao.observeUserSession().map { session ->
            if (session == null) return@map false
            val currentTime = Clock.System.now().toEpochMilliseconds()
            session.expirationTimestamp > currentTime
            // Side effect in flow map is generally discouraged but for this simple case it
            // might be ok.
            // Ideally we should handle expiration cleanup elsewhere or just return false here.
            // Let's just return false for now to be safe and pure.
            // Actual cleanup happens in getLoginSession or explicit logout.
        }
    }

    override suspend fun getLoginSession(): Boolean {
        val session = userSessionDao.getUserSession() ?: return false
        val currentTime = Clock.System.now().toEpochMilliseconds()
        return if (session.expirationTimestamp > currentTime) {
            true
        } else {
            userSessionDao.clearUserSession()
            false
        }
    }

    override suspend fun getAccount(): String {
        return userSessionDao.getUserSession()?.email.orEmpty()
    }

    override suspend fun login(email: String, password: String): Either<Exception, Unit> =
        withContext(Dispatchers.IO) {
            if (featureConfig.isServerEnabled.not()) {
                delay(1000L)
                // Mock session for local testing without server
                val expirationTime =
                    Clock.System.now().toEpochMilliseconds() + 24 * 60 * 60 * 1000 // 1 day
                userSessionDao.insertUserSession(
                    UserSessionEntity(
                        accessToken = "mock_token",
                        expirationTimestamp = expirationTime,
                        userId = "mock_user_id",
                        email = email,
                        name = "Mock User",
                    )
                )
                return@withContext Either.Right(Unit)
            }
            return@withContext try {
                val response = authRemoteDataSource.login(email = email, password = password)
                val expirationTime =
                    Clock.System.now().toEpochMilliseconds() + 24 * 60 * 60 * 1000 // 1 day
                userSessionDao.insertUserSession(
                    UserSessionEntity(
                        accessToken = response.login ?: "",
                        expirationTimestamp = expirationTime,
                        userId = "user_id_placeholder", // API might need to return this
                        email = email,
                        name = "User Name", // API might need to return this
                    )
                )
                Either.Right(Unit)
            } catch (e: Exception) {
                Either.Left(e)
            }
        }

    override suspend fun getUserRoles(): Either<Exception, List<UserRole>> =
        withContext(Dispatchers.IO) {
            if (featureConfig.isServerEnabled.not()) {
                delay(1000L)
                return@withContext Either.Right(UserRole.dummies)
            }
            return@withContext try {
                val data =
                    authRemoteDataSource.getRoles().getRoles!!.map { UserRole(it!!.id, it.name) }
                Either.Right(data)
            } catch (e: Exception) {
                Either.Left(e)
            }
        }

    override suspend fun register(param: RegisterParam): Either<Exception, Unit> =
        withContext(Dispatchers.IO) {
            if (featureConfig.isServerEnabled.not()) {
                delay(1000L)
                return@withContext Either.Right(Unit)
            }
            return@withContext try {
                authRemoteDataSource.createUser(
                    CreateUserMutation(
                        email = param.email,
                        password = param.password,
                        name = param.name,
                        phone = param.phone,
                        role_id = param.roleId.toInt(),
                    )
                )
                Either.Right(Unit)
            } catch (e: Exception) {
                Either.Left(e)
            }
        }

    override suspend fun verifyPin(pin: String): Boolean {
        // Mock PIN verification
        delay(500)
        return pin == "123456"
    }

    override suspend fun logout() {
        userSessionDao.clearUserSession()
    }
}

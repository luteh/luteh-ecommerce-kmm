package org.luteh.ecommerce.data.repository

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.LoginMutation
import org.luteh.ecommerce.data.config.FeatureConfig
import org.luteh.ecommerce.data.datasource.remote.AuthRemoteDataSource
import org.luteh.ecommerce.data.local.dao.UserSessionDao
import org.luteh.ecommerce.data.local.entity.UserSessionEntity
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock

class AuthRepositoryImplTest {

    private val authRemoteDataSource: AuthRemoteDataSource = mock()
    private val featureConfig: FeatureConfig = mock()
    private val userSessionDao: UserSessionDao = mock(MockMode.autoUnit)
    private val authRepository =
        AuthRepositoryImpl(authRemoteDataSource, featureConfig, userSessionDao)

    @Test
    fun `getLoginSession returns true when session is valid`() = runTest {
        val validExpiration = Clock.System.now().toEpochMilliseconds() + 10000
        val session =
            UserSessionEntity(
                accessToken = "token",
                expirationTimestamp = validExpiration,
                userId = "user1",
                email = "test@example.com",
                name = "Test User",
            )
        everySuspend { userSessionDao.getUserSession() } returns session

        val result = authRepository.getLoginSession()

        assertTrue(result)
    }

    @Test
    fun `getLoginSession returns false when session is expired`() = runTest {
        val expiredExpiration = Clock.System.now().toEpochMilliseconds() - 10000
        val session =
            UserSessionEntity(
                accessToken = "token",
                expirationTimestamp = expiredExpiration,
                userId = "user1",
                email = "test@example.com",
                name = "Test User",
            )
        everySuspend { userSessionDao.getUserSession() } returns session

        val result = authRepository.getLoginSession()

        assertFalse(result)
        verifySuspend { userSessionDao.clearUserSession() }
    }

    @Test
    fun `getLoginSession returns false when no session exists`() = runTest {
        everySuspend { userSessionDao.getUserSession() } returns null

        val result = authRepository.getLoginSession()

        assertFalse(result)
    }

    @Test
    fun `login saves session when successful`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val loginData = LoginMutation.Data(login = "token")

        everySuspend { featureConfig.isServerEnabled } returns true
        everySuspend { authRemoteDataSource.login(email, password) } returns loginData

        val result = authRepository.login(email, password)

        assertTrue(result.isRight())
        verifySuspend { userSessionDao.insertUserSession(any()) }
    }

    @Test
    fun `logout clears session`() = runTest {
        authRepository.logout()

        verifySuspend { userSessionDao.clearUserSession() }
    }
}

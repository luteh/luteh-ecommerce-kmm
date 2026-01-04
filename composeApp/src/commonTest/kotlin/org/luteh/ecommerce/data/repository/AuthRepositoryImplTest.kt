package org.luteh.ecommerce.data.repository

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.CreateUserMutation
import org.luteh.ecommerce.GetRolesQuery
import org.luteh.ecommerce.LoginMutation
import org.luteh.ecommerce.data.config.FeatureConfig
import org.luteh.ecommerce.data.datasource.remote.AuthRemoteDataSource
import org.luteh.ecommerce.data.local.database.dao.UserSessionDao
import org.luteh.ecommerce.data.local.entity.UserSessionEntity
import org.luteh.ecommerce.domain.model.RegisterParam
import org.luteh.ecommerce.domain.model.UserRole
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock

class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl
    private lateinit var authRemoteDataSource: AuthRemoteDataSource
    private lateinit var featureConfig: FeatureConfig
    private lateinit var userSessionDao: UserSessionDao

    @BeforeTest
    fun setUp() {
        authRemoteDataSource = mock(MockMode.autoUnit)
        featureConfig = mock(MockMode.autoUnit)
        userSessionDao = mock(MockMode.autoUnit)
        repository = AuthRepositoryImpl(authRemoteDataSource, featureConfig, userSessionDao)
    }

    @Test
    fun `getLoginSession should return true when session exists and is not expired`() = runTest {
        val futureTime = Clock.System.now().toEpochMilliseconds() + 10000
        val session =
            UserSessionEntity(
                accessToken = "token",
                expirationTimestamp = futureTime,
                userId = "user1",
                email = "test@example.com",
                name = "Test User",
            )
        everySuspend { userSessionDao.getUserSession() } returns session

        val result = repository.getLoginSession()

        assertTrue(result)
    }

    @Test
    fun `getLoginSession should return false and clear session when session is expired`() =
        runTest {
            val pastTime = Clock.System.now().toEpochMilliseconds() - 10000
            val session =
                UserSessionEntity(
                    accessToken = "token",
                    expirationTimestamp = pastTime,
                    userId = "user1",
                    email = "test@example.com",
                    name = "Test User",
                )
            everySuspend { userSessionDao.getUserSession() } returns session
            everySuspend { userSessionDao.clearUserSession() } returns Unit

            val result = repository.getLoginSession()

            assertFalse(result)
            verifySuspend { userSessionDao.clearUserSession() }
        }

    @Test
    fun `getLoginSession should return false when no session exists`() = runTest {
        everySuspend { userSessionDao.getUserSession() } returns null

        val result = repository.getLoginSession()

        assertFalse(result)
    }

    @Test
    fun `getAccount should return email when session exists`() = runTest {
        val session =
            UserSessionEntity(
                accessToken = "token",
                expirationTimestamp = 0,
                userId = "user1",
                email = "test@example.com",
                name = "Test User",
            )
        everySuspend { userSessionDao.getUserSession() } returns session

        val result = repository.getAccount()

        assertEquals("test@example.com", result)
    }

    @Test
    fun `getAccount should return empty string when no session exists`() = runTest {
        everySuspend { userSessionDao.getUserSession() } returns null

        val result = repository.getAccount()

        assertEquals("", result)
    }

    @Test
    fun `login should return success and save session when server is enabled and login succeeds`() =
        runTest {
            every { featureConfig.isServerEnabled } returns true
            val mutationResponse = LoginMutation.Data(login = "access_token")
            everySuspend { authRemoteDataSource.login(any(), any()) } returns mutationResponse
            everySuspend { userSessionDao.insertUserSession(any()) } returns Unit

            val result = repository.login("email", "password")

            assertTrue(result.isRight())
            verifySuspend { userSessionDao.insertUserSession(any()) }
        }

    @Test
    fun `login should return error when server is enabled and login fails`() = runTest {
        every { featureConfig.isServerEnabled } returns true
        val exception = RuntimeException("Login failed")
        everySuspend { authRemoteDataSource.login(any(), any()) } throws exception

        val result = repository.login("email", "password")

        assertTrue(result.isLeft())
        assertEquals(exception, result.leftOrNull())
    }

    @Test
    fun `login should return success and save mock session when server is disabled`() = runTest {
        every { featureConfig.isServerEnabled } returns false
        everySuspend { userSessionDao.insertUserSession(any()) } returns Unit

        val result = repository.login("email", "password")

        assertTrue(result.isRight())
        verifySuspend { userSessionDao.insertUserSession(any()) }
    }

    @Test
    fun `getUserRoles should return roles when server is enabled`() = runTest {
        every { featureConfig.isServerEnabled } returns true
        val roleData = GetRolesQuery.GetRole(id = "1", name = "Admin")
        val rolesResponse = GetRolesQuery.Data(getRoles = listOf(roleData))
        everySuspend { authRemoteDataSource.getRoles() } returns rolesResponse

        val result = repository.getUserRoles()

        assertTrue(result.isRight())
        val roles = result.getOrNull()
        assertEquals(1, roles?.size)
        assertEquals("1", roles?.first()?.id)
        assertEquals("Admin", roles?.first()?.name)
    }

    @Test
    fun `getUserRoles should return error when server is enabled and fetch fails`() = runTest {
        every { featureConfig.isServerEnabled } returns true
        val exception = RuntimeException("Fetch failed")
        everySuspend { authRemoteDataSource.getRoles() } throws exception

        val result = repository.getUserRoles()

        assertTrue(result.isLeft())
        assertEquals(exception, result.leftOrNull())
    }

    @Test
    fun `getUserRoles should return dummy roles when server is disabled`() = runTest {
        every { featureConfig.isServerEnabled } returns false

        val result = repository.getUserRoles()

        assertTrue(result.isRight())
        assertEquals(UserRole.dummies, result.getOrNull())
    }

    @Test
    fun `register should return success when server is enabled and registration succeeds`() =
        runTest {
            every { featureConfig.isServerEnabled } returns true
            everySuspend { authRemoteDataSource.createUser(any()) } returns
                CreateUserMutation.Data(
                    createUser =
                        CreateUserMutation.CreateUser(
                            id = "1",
                            name = "User",
                            email = "email",
                            phone = "phone",
                            role_id = 1,
                        )
                )

            val param = RegisterParam("name", "email", "password", "phone", "1")
            val result = repository.register(param)

            assertTrue(result.isRight())
            verifySuspend { authRemoteDataSource.createUser(any()) }
        }

    @Test
    fun `register should return error when server is enabled and registration fails`() = runTest {
        every { featureConfig.isServerEnabled } returns true
        val exception = RuntimeException("Registration failed")
        everySuspend { authRemoteDataSource.createUser(any()) } throws exception

        val param = RegisterParam("name", "email", "password", "phone", "1")
        val result = repository.register(param)

        assertTrue(result.isLeft())
        assertEquals(exception, result.leftOrNull())
    }

    @Test
    fun `register should return success when server is disabled`() = runTest {
        every { featureConfig.isServerEnabled } returns false

        val param = RegisterParam("name", "email", "password", "phone", "1")
        val result = repository.register(param)

        assertTrue(result.isRight())
    }

    @Test
    fun `verifyPin should return true for correct pin`() = runTest {
        val result = repository.verifyPin("123456")
        assertTrue(result)
    }

    @Test
    fun `verifyPin should return false for incorrect pin`() = runTest {
        val result = repository.verifyPin("000000")
        assertFalse(result)
    }

    @Test
    fun `logout should clear session`() = runTest {
        everySuspend { userSessionDao.clearUserSession() } returns Unit

        repository.logout()

        verifySuspend { userSessionDao.clearUserSession() }
    }
}

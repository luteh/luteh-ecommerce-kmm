package org.luteh.ecommerce.data.datasource.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.testing.QueueTestNetworkTransport
import com.apollographql.apollo.testing.enqueueTestResponse
import kotlinx.coroutines.test.runTest
import org.luteh.ecommerce.CreateUserMutation
import org.luteh.ecommerce.GetRolesQuery
import org.luteh.ecommerce.LoginMutation
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthRemoteDataSourceImplTest {

    private lateinit var apolloClient: ApolloClient
    private lateinit var dataSource: AuthRemoteDataSourceImpl

    @BeforeTest
    fun setUp() {
        apolloClient = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport()).build()
        dataSource = AuthRemoteDataSourceImpl(apolloClient)
    }

    @Test
    fun `login should return data when successful`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val loginData = LoginMutation.Data(login = "token")
        val operation = LoginMutation(email = email, password = password)

        apolloClient.enqueueTestResponse(operation, loginData)

        val result = dataSource.login(email, password)
        assertEquals(loginData, result)
    }

    @Test
    fun `login should throw exception when login is null`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val loginData = LoginMutation.Data(login = null)
        val operation = LoginMutation(email = email, password = password)

        apolloClient.enqueueTestResponse(operation, loginData)

        val exception = assertFailsWith<RuntimeException> { dataSource.login(email, password) }
        assertEquals("No user was found", exception.message)
    }

    @Test
    fun `login should throw exception when login is blank`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val loginData = LoginMutation.Data(login = "")
        val operation = LoginMutation(email = email, password = password)

        apolloClient.enqueueTestResponse(operation, loginData)

        val exception = assertFailsWith<RuntimeException> { dataSource.login(email, password) }
        assertEquals("No user was found", exception.message)
    }

    @Test
    fun `getRoles should return data when successful`() = runTest {
        val role = GetRolesQuery.GetRole(id = "1", name = "Admin")
        val rolesData = GetRolesQuery.Data(getRoles = listOf(role))
        val operation = GetRolesQuery()

        apolloClient.enqueueTestResponse(operation, rolesData)

        val result = dataSource.getRoles()
        assertEquals(rolesData, result)
    }

    @Test
    fun `getRoles should throw exception when roles list is null`() = runTest {
        val rolesData = GetRolesQuery.Data(getRoles = null)
        val operation = GetRolesQuery()

        apolloClient.enqueueTestResponse(operation, rolesData)

        val exception = assertFailsWith<RuntimeException> { dataSource.getRoles() }
        assertEquals("No roles was found", exception.message)
    }

    @Test
    fun `getRoles should throw exception when roles list is empty`() = runTest {
        val rolesData = GetRolesQuery.Data(getRoles = emptyList())
        val operation = GetRolesQuery()

        apolloClient.enqueueTestResponse(operation, rolesData)

        val exception = assertFailsWith<RuntimeException> { dataSource.getRoles() }
        assertEquals("No roles was found", exception.message)
    }

    @Test
    fun `createUser should return data when successful`() = runTest {
        val user =
            CreateUserMutation.CreateUser(
                id = "1",
                name = "User",
                email = "email",
                phone = "phone",
                role_id = 1,
            )
        val createUserData = CreateUserMutation.Data(createUser = user)
        val mutation =
            CreateUserMutation(
                name = "User",
                email = "email",
                password = "password",
                phone = "phone",
                role_id = 1,
            )

        apolloClient.enqueueTestResponse(mutation, createUserData)

        val result = dataSource.createUser(mutation)
        assertEquals(createUserData, result)
    }

    @Test
    fun `createUser should throw exception when createUser is null`() = runTest {
        val createUserData = CreateUserMutation.Data(createUser = null)
        val mutation =
            CreateUserMutation(
                name = "User",
                email = "email",
                password = "password",
                phone = "phone",
                role_id = 1,
            )

        apolloClient.enqueueTestResponse(mutation, createUserData)

        val exception = assertFailsWith<RuntimeException> { dataSource.createUser(mutation) }
        assertEquals("No user was found", exception.message)
    }
}

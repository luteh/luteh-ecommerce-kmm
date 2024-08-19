package org.luteh.ecommerce.data.datasource.remote

import com.apollographql.apollo.ApolloClient
import org.luteh.ecommerce.CreateUserMutation
import org.luteh.ecommerce.GetRolesQuery
import org.luteh.ecommerce.LoginMutation

class AuthRemoteDataSource(
    private val apolloClient: ApolloClient
) {

    suspend fun login(email: String, password: String): LoginMutation.Data {
        val data = apolloClient.mutation(LoginMutation(password = password, email = email))
            .execute().dataOrThrow()
        if (data.login.isNullOrBlank()) {
            throw RuntimeException("No user was found")
        }
        return data
    }

    suspend fun getRoles(): GetRolesQuery.Data {
        val data = apolloClient.query(GetRolesQuery()).execute().dataOrThrow()
        if (data.getRoles.isNullOrEmpty()) {
            throw RuntimeException("No roles was found")
        }
        return data
    }

    suspend fun createUser(mutation: CreateUserMutation): CreateUserMutation.Data {
        val data = apolloClient.mutation(mutation).execute().dataOrThrow()
        if (data.createUser == null) {
            throw RuntimeException("No user was found")
        }
        return data
    }
}
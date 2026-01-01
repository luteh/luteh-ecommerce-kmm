package org.luteh.ecommerce.data.datasource.remote

import org.luteh.ecommerce.CreateUserMutation
import org.luteh.ecommerce.GetRolesQuery
import org.luteh.ecommerce.LoginMutation

interface AuthRemoteDataSource {
    suspend fun login(email: String, password: String): LoginMutation.Data
    suspend fun getRoles(): GetRolesQuery.Data
    suspend fun createUser(mutation: CreateUserMutation): CreateUserMutation.Data
}

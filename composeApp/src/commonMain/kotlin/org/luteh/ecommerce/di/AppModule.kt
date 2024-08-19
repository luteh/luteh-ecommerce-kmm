package org.luteh.ecommerce.di

import com.apollographql.apollo.ApolloClient
import org.koin.dsl.module
import org.luteh.ecommerce.data.datasource.remote.AuthRemoteDataSource
import org.luteh.ecommerce.data.repository.AuthRepositoryImpl
import org.luteh.ecommerce.data.repository.ProductRepositoryImpl
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.domain.repository.ProductRepository
import org.luteh.ecommerce.getPlatform
import org.luteh.ecommerce.presentation.ui.login.LoginViewModel
import org.luteh.ecommerce.presentation.ui.register.RegisterViewModel

fun appModule() = module {
    factory { LoginViewModel(get()) }
    factory { RegisterViewModel(get()) }

    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl() }

    single { AuthRemoteDataSource(get()) }

    single { provideApolloClient() }
}

private fun provideApolloClient(
): ApolloClient {
    val serverUrl = if (getPlatform().isIos) {
        "http://localhost:4000/graphql"
    } else {
        "http://10.0.2.2:4000/graphql"
    }

    return ApolloClient.Builder()
        .serverUrl(serverUrl)
        .build();
}
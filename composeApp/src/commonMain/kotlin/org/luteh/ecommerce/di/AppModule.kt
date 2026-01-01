package org.luteh.ecommerce.di

import com.apollographql.apollo.ApolloClient
import org.koin.dsl.module
import org.luteh.ecommerce.data.config.FeatureConfig
import org.luteh.ecommerce.data.config.FeatureConfigImpl
import org.luteh.ecommerce.data.datasource.remote.AuthRemoteDataSource
import org.luteh.ecommerce.data.local.AppDatabase
import org.luteh.ecommerce.data.repository.AuthRepositoryImpl
import org.luteh.ecommerce.data.repository.CartRepositoryImpl
import org.luteh.ecommerce.data.repository.OrderRepositoryImpl
import org.luteh.ecommerce.data.repository.ProductRepositoryImpl
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.domain.repository.CartRepository
import org.luteh.ecommerce.domain.repository.OrderRepository
import org.luteh.ecommerce.domain.repository.ProductRepository
import org.luteh.ecommerce.domain.usecase.cart.UpdateCartItemQuantityUseCase
import org.luteh.ecommerce.getPlatform
import org.luteh.ecommerce.presentation.ui.cart.CartViewModel
import org.luteh.ecommerce.presentation.ui.checkout.CheckoutViewModel
import org.luteh.ecommerce.presentation.ui.login.LoginViewModel
import org.luteh.ecommerce.presentation.ui.product_detail.ProductDetailViewModel
import org.luteh.ecommerce.presentation.ui.register.RegisterViewModel
import org.luteh.ecommerce.presentation.ui.transaction_detail.TransactionDetailViewModel

fun appModule() = module {
    factory { LoginViewModel(get()) }
    factory { RegisterViewModel(get()) }
    factory { ProductDetailViewModel(get()) }
    factory { CartViewModel(get(), get()) }
    factory { CheckoutViewModel(get(), get()) }
    factory { TransactionDetailViewModel() }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl() }
    single<OrderRepository> { OrderRepositoryImpl(get()) }

    includes(platformModule())
    single { get<AppDatabase>().cartDao() }
    single { AuthRemoteDataSource(get()) }
    single<CartRepository> { CartRepositoryImpl(get()) }

    // Cart UseCases
    single { UpdateCartItemQuantityUseCase(get()) }

    single<FeatureConfig> { FeatureConfigImpl() }

    single { provideApolloClient() }
}

private fun provideApolloClient(): ApolloClient {
    val serverUrl =
        if (getPlatform().isIos) {
            "http://localhost:4000/graphql"
        } else {
            "http://10.0.2.2:4000/graphql"
        }

    return ApolloClient.Builder().serverUrl(serverUrl).build()
}

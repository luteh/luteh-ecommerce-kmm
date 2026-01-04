package org.luteh.ecommerce.di

import com.apollographql.apollo.ApolloClient
import org.koin.dsl.module
import org.luteh.ecommerce.data.config.FeatureConfig
import org.luteh.ecommerce.data.config.FeatureConfigImpl
import org.luteh.ecommerce.data.datasource.remote.AuthRemoteDataSource
import org.luteh.ecommerce.data.datasource.remote.AuthRemoteDataSourceImpl
import org.luteh.ecommerce.data.local.database.AppDatabase
import org.luteh.ecommerce.data.repository.AddressRepositoryImpl
import org.luteh.ecommerce.data.repository.AuthRepositoryImpl
import org.luteh.ecommerce.data.repository.CartRepositoryImpl
import org.luteh.ecommerce.data.repository.OrderRepositoryImpl
import org.luteh.ecommerce.data.repository.ProductRepositoryImpl
import org.luteh.ecommerce.domain.repository.AddressRepository
import org.luteh.ecommerce.domain.repository.AuthRepository
import org.luteh.ecommerce.domain.repository.CartRepository
import org.luteh.ecommerce.domain.repository.OrderRepository
import org.luteh.ecommerce.domain.repository.ProductRepository
import org.luteh.ecommerce.domain.usecase.auth.CheckSessionUseCase
import org.luteh.ecommerce.domain.usecase.auth.CheckSessionUseCaseImpl
import org.luteh.ecommerce.domain.usecase.auth.LoginUseCase
import org.luteh.ecommerce.domain.usecase.auth.LogoutUseCase
import org.luteh.ecommerce.domain.usecase.cart.UpdateCartItemQuantityUseCase
import org.luteh.ecommerce.domain.usecase.cart.UpdateCartItemQuantityUseCaseImpl
import org.luteh.ecommerce.getPlatform
import org.luteh.ecommerce.presentation.ui.cart.CartViewModel
import org.luteh.ecommerce.presentation.ui.checkout.CheckoutViewModel
import org.luteh.ecommerce.presentation.ui.login.LoginViewModel
import org.luteh.ecommerce.presentation.ui.product_detail.ProductDetailViewModel
import org.luteh.ecommerce.presentation.ui.product_list.ProductListViewModel
import org.luteh.ecommerce.presentation.ui.register.RegisterViewModel
import org.luteh.ecommerce.presentation.ui.transaction_detail.TransactionDetailViewModel

fun appModule() = module {
    factory { LoginViewModel(get()) }
    factory { RegisterViewModel(get()) }
    factory { ProductDetailViewModel(get(), get()) }
    factory { ProductListViewModel(get()) }
    factory { CartViewModel(get(), get()) }
    factory { CheckoutViewModel(get(), get(), get(), get()) }
    factory { TransactionDetailViewModel() }

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl() }
    single<OrderRepository> { OrderRepositoryImpl(get()) }
    single<AddressRepository> { AddressRepositoryImpl(get()) }

    includes(platformModule())
    single { get<AppDatabase>().cartDao() }
    single { get<AppDatabase>().shippingAddressDao() }
    single { get<AppDatabase>().userSessionDao() }
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get()) }
    single<CartRepository> { CartRepositoryImpl(get()) }

    // Cart UseCases
    single<UpdateCartItemQuantityUseCase> { UpdateCartItemQuantityUseCaseImpl(get()) }
    factory { LoginUseCase(get()) }
    factory<CheckSessionUseCase> { CheckSessionUseCaseImpl(get()) }
    factory { LogoutUseCase(get()) }

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

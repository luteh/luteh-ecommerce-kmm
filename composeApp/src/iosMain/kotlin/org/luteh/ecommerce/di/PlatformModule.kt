package org.luteh.ecommerce.di

import org.koin.dsl.module
import org.luteh.ecommerce.data.local.getDatabaseBuilder
import org.luteh.ecommerce.data.local.AppDatabase

actual fun platformModule() = module {
    single<AppDatabase> { getDatabaseBuilder().build() }
}


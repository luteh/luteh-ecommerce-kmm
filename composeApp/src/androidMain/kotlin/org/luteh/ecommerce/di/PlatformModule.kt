package org.luteh.ecommerce.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.luteh.ecommerce.data.local.database.AppDatabase
import org.luteh.ecommerce.data.local.getDatabaseBuilder

actual fun platformModule() = module {
    single<AppDatabase> { getDatabaseBuilder(androidContext()).build() }
}

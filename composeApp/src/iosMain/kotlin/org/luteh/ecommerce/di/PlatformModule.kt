package org.luteh.ecommerce.di

import org.koin.dsl.module
import org.luteh.ecommerce.data.local.AppDatabase
import org.luteh.ecommerce.data.local.getDatabaseBuilder

actual fun platformModule() = module { single<AppDatabase> { getDatabaseBuilder().build() } }

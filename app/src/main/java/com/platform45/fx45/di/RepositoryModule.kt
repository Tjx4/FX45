package com.platform45.fx45.di

import xyz.appic.repositories.FXRepository
import xyz.appic.repositories.IFXRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<IFXRepository> { FXRepository(get(), get(), get()) }
}
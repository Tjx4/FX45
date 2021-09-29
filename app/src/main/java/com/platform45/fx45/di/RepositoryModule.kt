package com.platform45.fx45.di

import com.platform45.fx45.repositories.FXRepository
import com.platform45.fx45.repositories.IFXRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<IFXRepository> { FXRepository(get(), get(), get()) }
}
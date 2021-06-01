package com.platform45.fx45.di

import com.platform45.fx45.repositories.FXRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { FXRepository(get(), get()) }
}
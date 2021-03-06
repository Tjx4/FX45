package com.platform45.fx45.di

import com.platform45.fx45.networking.retrofit.API
import com.platform45.fx45.persistance.room.FX45Db
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val networkingModule = module {
    single { API.retrofit }
}

val persistenceModule = module {
    single { FX45Db.getInstance(androidApplication())}
}
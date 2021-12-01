package com.platform45.fx45.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import xyz.appic.core.retrofit.API
import xyz.appic.core.persistance.room.FX45Db
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val networkingModule = module {
    single { API.retrofit }
}

val persistenceModule = module {
    single { FX45Db.getInstance(androidApplication())}
}

val analyticsModule = module {
    single { FirebaseCrashlytics.getInstance()}
}
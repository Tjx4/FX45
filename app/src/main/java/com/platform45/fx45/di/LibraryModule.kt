package com.platform45.fx45.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.platform45.fx45.networking.retrofit.API
import com.platform45.fx45.persistance.room.FX45Db
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val libraryModule = module {
    single { API.retrofit }
    single { FX45Db.getInstance(androidApplication()) }
    single { FirebaseCrashlytics.getInstance() }
}
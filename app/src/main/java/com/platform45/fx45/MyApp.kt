package com.platform45.fx45

import android.app.Application
import com.platform45.fx45.di.ModuleLoadHelper
import com.platform45.fx45.di.networkingModule
import com.platform45.fx45.di.repositoryModule
import com.platform45.fx45.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    viewModelModule,
                    repositoryModule,
                    networkingModule
                ) + ModuleLoadHelper.getBuildSpecialModuleList()
            )
        }
    }
}
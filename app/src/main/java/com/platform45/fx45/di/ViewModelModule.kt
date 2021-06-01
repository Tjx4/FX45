package com.platform45.fx45.di

import com.platform45.fx45.ui.dashboard.DashboardViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashboardViewModel(get()) }
    //viewModel { ConversionViewModel(androidApplication(), get()) }
}

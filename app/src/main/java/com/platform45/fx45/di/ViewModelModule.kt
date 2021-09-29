package com.platform45.fx45.di

import com.platform45.fx45.ui.confirm.ConFirmViewModel
import com.platform45.fx45.ui.convert.ConversionViewModel
import com.platform45.fx45.ui.dashboard.DashboardViewModel
import com.platform45.fx45.ui.tradeHistory.TradeHistoryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashboardViewModel(androidApplication(), get()) }
    viewModel { ConFirmViewModel(androidApplication(), get()) }
    viewModel { ConversionViewModel(androidApplication(), get()) }
    viewModel { TradeHistoryViewModel(androidApplication(), get()) }
}

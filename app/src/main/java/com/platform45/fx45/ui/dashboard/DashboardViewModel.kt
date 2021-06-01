package com.platform45.fx45.ui.dashboard

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.repositories.FXRepository
import com.platform45.fx45.ui.dashboard.pagging.PopularPairPagingSource

class DashboardViewModel(val app: Application, val fXRepository: FXRepository) : BaseVieModel(app) {

    val popularCurrencyPairs = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { PopularPairPagingSource(fXRepository) }
    ).flow.cachedIn(viewModelScope)

}
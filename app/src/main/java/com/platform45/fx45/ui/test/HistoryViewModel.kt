package com.platform45.fx45.ui.test

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.repositories.FXRepository
import com.platform45.fx45.ui.test.pagging.HistoryPairPagingSource

class HistoryViewModel(val app: Application, private val fXRepository: FXRepository) : BaseVieModel(app) {

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    var startDate: String = ""
    var endDate: String = ""
    var currencyPairs: String = ""

    val popularCurrencyPairs = Pager(config = PagingConfig(pageSize = 10)) {
        HistoryPairPagingSource(startDate, endDate, currencyPairs, fXRepository)
    }.flow.cachedIn(viewModelScope)

}
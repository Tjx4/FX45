package com.platform45.fx45.ui.tradeHistory

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.constants.H_PAGE_SIZE
import com.platform45.fx45.constants.PP_PAGE_SIZE
import com.platform45.fx45.repositories.FXRepository
import com.platform45.fx45.ui.tradeHistory.pagging.HistoryPairPagingSource

class TradeHistoryViewModel(val app: Application, private val fXRepository: FXRepository) : BaseVieModel(app) {

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _pairsList: MutableLiveData<List<String>> = MutableLiveData()
    val pairsList: MutableLiveData<List<String>>
        get() = _pairsList

    var startDate: String = ""
    var endDate: String = ""
    var currencyPairs: String = ""

    val popularCurrencyPairs = Pager(config = PagingConfig(pageSize = H_PAGE_SIZE)) {
        HistoryPairPagingSource(startDate, endDate, currencyPairs, fXRepository)
    }.flow.cachedIn(viewModelScope)

    fun setParams(startDate: String, endDate: String, currencyPairs: String){
        this.startDate = startDate
        this.endDate = endDate
        this.currencyPairs  = currencyPairs
    }

    fun setPairsList(currencyPairs: String){
        pairsList.value = currencyPairs.split(",")
    }

}
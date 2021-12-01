package com.platform45.fx45.ui.tradeHistory

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.platform45.fx45.base.viewmodel.SharedViewModel
import xyz.appic.common.constants.H_PAGE_SIZE
import xyz.appic.repositories.IFXRepository
import com.platform45.fx45.ui.tradeHistory.paging.HistoryPairPagingSource

class TradeHistoryViewModel(application: Application, private val fXRepository: IFXRepository) : SharedViewModel(application) {

    private val _currencyPairs: MutableLiveData<String> = MutableLiveData("")
    val currencyPairs: MutableLiveData<String>
        get() = _currencyPairs

    val popularCurrencyPairs = Pager(config = PagingConfig(pageSize = H_PAGE_SIZE)) {
        HistoryPairPagingSource(_startDate.value!!, _endDate.value!!, _currencyPairs.value!!, fXRepository)
    }.flow.cachedIn(viewModelScope)

    fun setParams(startDate: String, endDate: String, currencyPairs: String){
        _startDate.value = startDate
        _endDate.value = endDate
        _currencyPairs.value  = currencyPairs
    }

}
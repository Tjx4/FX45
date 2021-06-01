package com.platform45.fx45.ui.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.helpers.getCurrentDate
import com.platform45.fx45.helpers.getDaysAgo
import com.platform45.fx45.models.CurrencyPair
import com.platform45.fx45.repositories.FXRepository
import com.platform45.fx45.ui.dashboard.pagging.PopularPairPagingSource
import java.util.*
import kotlin.collections.ArrayList

class DashboardViewModel(val app: Application, private val fXRepository: FXRepository) : BaseVieModel(app) {
    val availableCurrencies: MutableLiveData<List<String>> = MutableLiveData()

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _canProceed: MutableLiveData<Boolean> = MutableLiveData()
    val canProceed: MutableLiveData<Boolean>
        get() = _canProceed

    private val _startDate: MutableLiveData<String> = MutableLiveData()
    val startDate: MutableLiveData<String>
        get() = _startDate

    private val _endDate: MutableLiveData<String> = MutableLiveData()
    val endDate: MutableLiveData<String>
        get() = _endDate

    private val _userSelectedPair: MutableLiveData<String> = MutableLiveData()
    val userSelectedPair: MutableLiveData<String>
        get() = _userSelectedPair

    private val _currencyPairs: MutableLiveData<List<String>> = MutableLiveData()
    val currencyPairs: MutableLiveData<List<String>>
        get() = _currencyPairs

    private val _pairsMessage: MutableLiveData<String> = MutableLiveData()
    val pairsMessage: MutableLiveData<String>
        get() = _pairsMessage

    private val _isPairsUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val isPairsUpdated: MutableLiveData<Boolean>
        get() = _isPairsUpdated

    val popularCurrencyPairs = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { PopularPairPagingSource(fXRepository, _currencyPairs.value) }
    ).flow.cachedIn(viewModelScope)

    init {
        initCurrencies()
        initCurrencyPairs()
        initStartAndEndDate()
    }

    private fun initStartAndEndDate() {
        _startDate.value = getDaysAgo(30)
        _endDate.value = getCurrentDate()
    }

    fun checkState() {
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
    }

    private fun initCurrencyPairs() {
        _currencyPairs.value = ArrayList()
    }

    fun initCurrencies() {
        val tmpList = ArrayList<String>()
        for (currency in Currency.getAvailableCurrencies()) {
            tmpList.add(currency.currencyCode)
        }
        availableCurrencies.value = tmpList?.sortedBy { it }
    }

    fun setCurrencyPair(frmIndx: Int, ToIndx: Int) {
        _userSelectedPair.value = "${availableCurrencies.value?.get(frmIndx) ?: ""}${availableCurrencies.value?.get(ToIndx)}"
    }

    fun addCreatedPairToList() {
        _userSelectedPair.value?.let { addCurrencyPairToList(it) }
    }

    fun addPopularPairToList(currencyPair: String) {
        currencyPair.let { addCurrencyPairToList(it) }
    }

    private fun addCurrencyPairToList(currencyPair: String) {
        val currencyPairs = _currencyPairs.value as ArrayList
        currencyPairs.add(currencyPair)
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
        _pairsMessage.value = "You selected ${currencyPairs.size} currency pair${if(currencyPairs.size == 1) "" else "s"}"
        _isPairsUpdated.value = true
    }

    fun deleteCurrencyPairFromList(indx: Int) {
        val currencyPairs = _currencyPairs.value as ArrayList
        currencyPairs.removeAt(indx)
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
        _pairsMessage.value = "You selected ${currencyPairs.size} pair${if(currencyPairs.size == 1) "" else "s"}"
        _isPairsUpdated.value = true
    }
}
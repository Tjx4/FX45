package com.platform45.fx45.ui.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.constants.PP_PAGE_SIZE
import com.platform45.fx45.helpers.getClosestWeekDay
import com.platform45.fx45.helpers.getCurrentDate
import com.platform45.fx45.repositories.IFXRepository
import com.platform45.fx45.ui.dashboard.paging.PopularPairPagingSource
import java.util.*
import kotlin.collections.ArrayList

class DashboardViewModel(application: Application, private val fXRepository: IFXRepository) : BaseVieModel(application) {
    val availableCurrencies: MutableLiveData<List<String>> = MutableLiveData()

    private val _canProceed: MutableLiveData<Boolean> = MutableLiveData()
    val canProceed: MutableLiveData<Boolean>
        get() = _canProceed

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

    val popularCurrencyPairs = Pager(config = PagingConfig(pageSize = PP_PAGE_SIZE)) {
        PopularPairPagingSource(fXRepository)
    }.flow.cachedIn(viewModelScope)

    init {
        _currencyPairs.value = ArrayList()
    }


    fun checkState() {
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
    }

    fun setCurrencyPair(frmIndx: Int, toIndx: Int) {
        _userSelectedPair.value = "${availableCurrencies.value?.get(frmIndx) ?: ""}${availableCurrencies.value?.get(toIndx)}"
    }

    fun addCreatedPairToList() {
        _userSelectedPair.value?.let { addCurrencyPairToList(it) }
    }

    fun togglePopularPairFromList(currencyPair: String) {
        val currencyPairs = _currencyPairs.value as ArrayList
        if(currencyPairs.contains(currencyPair))
            removePairFromList(currencyPair)
        else
            addCurrencyPairToList(currencyPair)
    }

    private fun addCurrencyPairToList(currencyPair: String) {
        val currencyPairs = _currencyPairs.value as ArrayList
        currencyPairs.add(currencyPair)
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
        _pairsMessage.value = "You selected ${currencyPairs.size} currency pair${if(currencyPairs.size == 1) "" else "s"}"
        _isPairsUpdated.value = true
    }

    fun removePairFromList(indx: Int) {
        val currencyPairs = _currencyPairs.value as ArrayList
        if(indx > currencyPairs.size) return
        currencyPairs.removeAt(indx)
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
        _pairsMessage.value = "You selected ${currencyPairs.size} pair${if(currencyPairs.size == 1) "" else "s"}"
        _isPairsUpdated.value = true
    }

    fun removePairFromList(pair: String) {
        val currencyPairs = _currencyPairs.value as ArrayList
        currencyPairs.remove(pair)
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
        _pairsMessage.value = "You selected ${currencyPairs.size} pair${if(currencyPairs.size == 1) "" else "s"}"
        _isPairsUpdated.value = true
    }

    fun getCurrencyPairsString(): String{
        var currency = ""
        _currencyPairs.value?.let {
            for((index, pair) in it.withIndex()) {
                currency += if (index > 0) ",$pair" else "$pair"
            }
        }
        return currency
    }

}
package com.platform45.fx45.ui.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.constants.PP_PAGE_SIZE
import com.platform45.fx45.repositories.IFXRepository
import com.platform45.fx45.ui.dashboard.paging.PopularPairPagingSource
import kotlin.collections.ArrayList

class DashboardViewModel(application: Application, private val fXRepository: IFXRepository) : BaseVieModel(application) {

    private val _canProceed: MutableLiveData<Boolean> = MutableLiveData()
    val canProceed: MutableLiveData<Boolean>
        get() = _canProceed

    private val _hideProceed: MutableLiveData<Boolean> = MutableLiveData()
    val hideProceed: MutableLiveData<Boolean>
        get() = _hideProceed

    private val _userSelectedPair: MutableLiveData<String> = MutableLiveData()
    val userSelectedPair: MutableLiveData<String>
        get() = _userSelectedPair

    private val _selectedCurrencyPairs: MutableLiveData<List<String>> = MutableLiveData(ArrayList())
    val currencyPairs: MutableLiveData<List<String>>
        get() = _selectedCurrencyPairs

    private val _pairsMessage: MutableLiveData<String> = MutableLiveData()
    val pairsMessage: MutableLiveData<String>
        get() = _pairsMessage

    val popularCurrencyPairs = Pager(config = PagingConfig(pageSize = PP_PAGE_SIZE)) {
        PopularPairPagingSource(fXRepository)
    }.flow.cachedIn(viewModelScope)

    fun togglePopularPairFromList(currencyPair: String) {
        _selectedCurrencyPairs.value?.let {
            if(it.contains(currencyPair)) {
                removePairFromList(currencyPair)
                _pairsMessage.value = "You selected ${it.size} pair${if(it.size == 1) "" else "s"}"
            }else {
                addCurrencyPairToList(currencyPair)
                _pairsMessage.value = "You selected ${it.size} currency pair${if(it.size == 1) "" else "s"}"
            }
        }
    }

    fun toggleStatus() {
        _selectedCurrencyPairs.value?.let {
            when {
                it.isNullOrEmpty() -> _hideProceed.value = true
                else -> _canProceed.value = true
            }
        }
    }

    private fun addCurrencyPairToList(pair: String) {
        _selectedCurrencyPairs.value?.let {
            (it as ArrayList).add(pair)
        }
    }

    fun removePairFromList(pair: String) {
        _selectedCurrencyPairs.value?.let {
            (it as ArrayList).remove(pair)
        }
    }

/*

    fun checkState() {
        _canProceed.value = !_currencyPairs.value.isNullOrEmpty()
    }

    fun addCreatedPairToList() {
        _userSelectedPair.value?.let { addCurrencyPairToList(it) }
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

*/

}
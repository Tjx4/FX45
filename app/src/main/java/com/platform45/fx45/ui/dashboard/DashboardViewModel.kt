package com.platform45.fx45.ui.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.platform45.fx45.R
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

    private val _selectedCurrencyPairs: MutableLiveData<List<String>> = MutableLiveData(ArrayList())
    val currencyPairs: MutableLiveData<List<String>>
        get() = _selectedCurrencyPairs

    private val _selectedPairMessage: MutableLiveData<String> = MutableLiveData()
    val selectedPairMessage: MutableLiveData<String>
        get() = _selectedPairMessage

    val popularCurrencyPairs = Pager(config = PagingConfig(pageSize = PP_PAGE_SIZE)) {
        PopularPairPagingSource(fXRepository)
    }.flow.cachedIn(viewModelScope)

    fun togglePopularPairFromList(currencyPair: String) {
        _selectedCurrencyPairs.value?.let {
            when {
                it.contains(currencyPair) -> {
                    removePairFromList(currencyPair)
                }
                else -> {
                    addCurrencyPairToList(currencyPair)
                    _selectedPairMessage.value = app.getString(R.string.added_pair, currencyPair)
                }
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

}
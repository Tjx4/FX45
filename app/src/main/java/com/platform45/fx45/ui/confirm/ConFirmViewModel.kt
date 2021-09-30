package com.platform45.fx45.ui.confirm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.base.viewmodel.SharedViewModel
import com.platform45.fx45.helpers.getClosestWeekDay
import com.platform45.fx45.helpers.getCurrentDate
import com.platform45.fx45.repositories.IFXRepository
import java.util.*
import kotlin.collections.ArrayList

class ConFirmViewModel(application: Application, val fXRepository: IFXRepository) : SharedViewModel(application) {

    private val _pairsMessage: MutableLiveData<String> = MutableLiveData()
    val pairsMessage: MutableLiveData<String>
        get() = _pairsMessage

    private val _userSelectedPair: MutableLiveData<String> = MutableLiveData()
    val userSelectedPair: MutableLiveData<String>
        get() = _userSelectedPair

    init {
        initStartAndEndDate()
        initCurrencies()
    }

    fun initStartAndEndDate() {
        _startDate.value = getClosestWeekDay(30)
        _endDate.value = getCurrentDate()
    }

    fun initCurrencies() {
        val tmpList = ArrayList<String>()
        for (currency in Currency.getAvailableCurrencies()) {
            tmpList.add(currency.currencyCode)
        }
        selectedPairs.value = tmpList?.sortedBy { it }
    }

    fun setStartDate(startDate: String?) {
        _startDate.value = "$startDate"
    }

    fun setEndDate(endDate: String?) {
        _endDate.value = "$endDate"
    }

    fun setStartTime(startTime: String?) {
        startTime?.let { _startDate.value = "${_startDate.value}-$it" }
    }

    fun setEndTime(endTime: String?) {
        endTime.let { _endDate.value = "${_endDate.value}-$it"}
    }

    fun setCurrencyPair(frmIndx: Int, toIndx: Int) {
        _userSelectedPair.value = "${_selectedPairs.value?.get(frmIndx) ?: ""}${_selectedPairs.value?.get(toIndx)}"
    }


/*
    fun togglePopularPairFromList() {
        _availableCurrencies.value?.let {
            when {
                it.(currencyPair) -> {
                    _pairsMessage.value = "You selected ${it.size} pair${if (it.size == 1) "" else "s"}"
                }
                else -> {
                    _pairsMessage.value = "You selected ${it.size} currency pair${if (it.size == 1) "" else "s"}"
                }
            }
        }
    }



*/
}
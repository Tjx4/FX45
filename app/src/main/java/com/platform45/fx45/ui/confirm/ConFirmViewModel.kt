package com.platform45.fx45.ui.confirm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.platform45.fx45.base.viewmodel.SharedViewModel
import xyz.appic.common.helpers.getClosestWeekDay
import xyz.appic.common.helpers.getCurrentDate
import xyz.appic.repositories.IFXRepository
import java.util.*
import kotlin.collections.ArrayList

class ConFirmViewModel(application: Application, val fXRepository: IFXRepository) : SharedViewModel(application) {

    private val _pairsMessage: MutableLiveData<String> = MutableLiveData()
    val pairsMessage: MutableLiveData<String>
        get() = _pairsMessage

    private val _userSelectedPair: MutableLiveData<String> = MutableLiveData()
    val userSelectedPair: MutableLiveData<String>
        get() = _userSelectedPair

    private val _availableCurrencies: MutableLiveData<List<String>> = MutableLiveData()
    val availableCurrencies: MutableLiveData<List<String>>
        get() = _availableCurrencies

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
        _availableCurrencies.value = tmpList?.sortedBy { it }
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

}
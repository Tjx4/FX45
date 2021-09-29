package com.platform45.fx45.ui.confirm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.helpers.getClosestWeekDay
import com.platform45.fx45.helpers.getCurrentDate
import com.platform45.fx45.repositories.IFXRepository
import java.util.*
import kotlin.collections.ArrayList

class ConFirmViewModel(application: Application, val fXRepository: IFXRepository) : BaseVieModel(application) {

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _startDate: MutableLiveData<String> = MutableLiveData()
    val startDate: MutableLiveData<String>
        get() = _startDate

    private val _endDate: MutableLiveData<String> = MutableLiveData()
    val endDate: MutableLiveData<String>
        get() = _endDate

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
        availableCurrencies.value = tmpList?.sortedBy { it }
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
        _userSelectedPair.value = "${_availableCurrencies.value?.get(frmIndx) ?: ""}${_availableCurrencies.value?.get(toIndx)}"
    }

}
package com.platform45.fx45.base.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData

abstract class SharedViewModel(application: Application) : BaseVieModel(application) {
    protected val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    protected val _startDate: MutableLiveData<String> = MutableLiveData("")
    val startDate: MutableLiveData<String>
        get() = _startDate

    protected val _endDate: MutableLiveData<String> = MutableLiveData("")
    val endDate: MutableLiveData<String>
        get() = _endDate

    protected val _selectedPairs: MutableLiveData<List<String>> = MutableLiveData(ArrayList())
    val selectedPairs: MutableLiveData<List<String>>
        get() = _selectedPairs

    fun getCurrencyPairsString(): String{
        var currency = ""
        _selectedPairs.value?.let {
            for((index, pair) in it.withIndex()) {
                currency += if (index > 0) ",$pair" else "$pair"
            }
        }
        return currency
    }

    fun setPairsList(currencyPairs: String){
        _selectedPairs.value = currencyPairs.split(",")
    }

    fun removePairFromList(pair: String) {
        _selectedPairs.value?.let {
            (it as ArrayList).remove(pair)
        }
    }

}
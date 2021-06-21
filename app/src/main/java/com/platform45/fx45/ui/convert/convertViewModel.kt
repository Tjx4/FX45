package com.platform45.fx45.ui.convert

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.platform45.fx45.R
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.repositories.FXRepository
import com.platform45.fx45.models.Conversion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConversionViewModel(application: Application, private val fXRepository: FXRepository) : BaseVieModel(application) {

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _from: MutableLiveData<String> = MutableLiveData()
    val from: MutableLiveData<String>
        get() = _from

    private val _to: MutableLiveData<String> = MutableLiveData()
    val to: MutableLiveData<String>
        get() = _to

    private val _amount: MutableLiveData<String> = MutableLiveData()
    val amount: MutableLiveData<String>
        get() = _amount

    private val _convert: MutableLiveData<Conversion?> = MutableLiveData()
    val convert: MutableLiveData<Conversion?>
        get() = _convert

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<String>
        get() = _error

    init {
        _amount.value = "1"
    }

    fun checkAndConvert(){
        _showLoading.value = true
        val from = _from.value
        val to = _to.value
        val amount = _amount.value

        when{
            from.isNullOrEmpty() -> _error.value = app.getString(R.string.from_convert_error)
            to.isNullOrEmpty() -> _error.value = app.getString(R.string.to_convert_error)
            amount.isNullOrEmpty() -> _error.value = app.getString(R.string.amount_convert_error)
            else -> convertCurrency(from, to, amount)
        }
    }

    fun presetCurrencies(from: String?, to: String?) {
        from.let { _from.value = it }
        to.let { _to.value = it }
    }

    fun convertCurrency(from: String, to: String, amount: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val conversion = fXRepository.getConversion(API_KEY, from, to, amount)

            withContext(Dispatchers.Main) {
                if (conversion != null) {
                    _convert.value = conversion
                }
            }
        }
    }

}
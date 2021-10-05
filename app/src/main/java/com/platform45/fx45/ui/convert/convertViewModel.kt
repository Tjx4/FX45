package com.platform45.fx45.ui.convert

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.platform45.fx45.R
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.models.Conversion
import com.platform45.fx45.repositories.IFXRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConversionViewModel(application: Application, val fXRepository: IFXRepository) : BaseVieModel(application) {

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _isValidInput: MutableLiveData<Boolean> = MutableLiveData()
    val isValidInput: MutableLiveData<Boolean>
        get() = _isValidInput

    private val _from: MutableLiveData<String> = MutableLiveData()
    val from: MutableLiveData<String>
        get() = _from

    private val _to: MutableLiveData<String> = MutableLiveData()
    val to: MutableLiveData<String>
        get() = _to

    private val _amount: MutableLiveData<String> = MutableLiveData<String>("1")
    val amount: MutableLiveData<String>
        get() = _amount

    private val _conversion: MutableLiveData<Conversion?> = MutableLiveData()
    val conversion: MutableLiveData<Conversion?>
        get() = _conversion

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<String>
        get() = _error

    private val _dialogErrorMessage: MutableLiveData<String> = MutableLiveData()
    val dialogErrorMessage: MutableLiveData<String>
        get() = _dialogErrorMessage

    fun showLoaderAndVerify() {
        _showLoading.value = true
        verifyInput(_from.value ?: "", _to.value ?: "", _amount.value ?: "")
    }

    fun presetCurrencyPair(from: String, to: String) {
        _from.value = from
        _to.value = to
    }

    fun verifyInput(from: String, to: String, amount: String) {
        when {
            from.isNullOrEmpty() -> _error.value = app.getString(R.string.from_convert_error)
            to.isNullOrEmpty() -> _error.value = app.getString(R.string.to_convert_error)
            amount.isNullOrEmpty() -> _error.value = app.getString(R.string.amount_convert_error)
            else -> _isValidInput.value = true
        }
    }

    suspend fun convertCurrency(from: String, to: String, amount: String) {
        val conversion = fXRepository.getConversion(API_KEY, from, to, amount)

        withContext(Dispatchers.Main) {
            when {
                conversion == null -> _dialogErrorMessage.value = app.getString(R.string.convert_response_error)
                conversion?.error != null -> _dialogErrorMessage.value = conversion?.error?.info
                else -> _conversion.value = conversion
            }
        }
    }

}
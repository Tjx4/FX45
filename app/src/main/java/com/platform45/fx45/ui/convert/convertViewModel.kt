package com.platform45.fx45.ui.convert

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.platform45.fx45.R
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.models.Conversion
import com.platform45.fx45.repositories.IFXRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConversionViewModel(application: Application, val fXRepository: IFXRepository) : BaseVieModel(application) {

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

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: MutableLiveData<String>
        get() = _errorMessage

    private val _dialogErrorMessage: MutableLiveData<String> = MutableLiveData()
    val dialogErrorMessage: MutableLiveData<String>
        get() = _dialogErrorMessage

    init {
        _amount.value = "1"
    }

    //Todo refactor
    fun showLoaderAndConvert(){
        _showLoading.value = true
        checkAndConvert(_from.value ?: "", _to.value ?: "", _amount.value ?: "")
    }

    fun presetCurrencies(from: String?, to: String?) {
        from.let { _from.value = it }
        to.let { _to.value = it }
    }

    fun checkAndConvert(from: String, to: String, amount: String){
        when{
            from.isNullOrEmpty() -> _errorMessage.value = app.getString(R.string.from_convert_error)
            to.isNullOrEmpty() -> _errorMessage.value = app.getString(R.string.to_convert_error)
            amount.isNullOrEmpty() -> _errorMessage.value = app.getString(R.string.amount_convert_error)
            else -> viewModelScope.launch(Dispatchers.IO) { convertCurrency(from, to, amount)}
        }
    }

   suspend fun convertCurrency(from: String, to: String, amount: String) {
        val response = fXRepository.getConversion("API_KEY", from, to, amount)

        withContext(Dispatchers.Main) {
            if (response.isSuccessful && response.body() != null) {
                _convert.value = response.body() as Conversion
            }
            else{
                _dialogErrorMessage.value = response.errorBody()?.string()
            }
        }
    }

}
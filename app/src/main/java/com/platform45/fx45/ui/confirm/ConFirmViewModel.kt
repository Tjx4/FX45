package com.platform45.fx45.ui.confirm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.platform45.fx45.base.viewmodel.BaseVieModel
import com.platform45.fx45.repositories.IFXRepository

class ConFirmViewModel(application: Application, val fXRepository: IFXRepository) : BaseVieModel(application) {

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading
}
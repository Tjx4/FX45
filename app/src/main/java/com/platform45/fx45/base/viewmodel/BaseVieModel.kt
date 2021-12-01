package com.platform45.fx45.base.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

abstract class BaseVieModel(protected val app: Application) : AndroidViewModel(app){
}
package com.platform45.fx45.base.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.platform45.fx45.MyDrawerController

abstract class BaseFragment : Fragment() {
    protected lateinit var myDrawerController: MyDrawerController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController = activity as MyDrawerController
    }
}
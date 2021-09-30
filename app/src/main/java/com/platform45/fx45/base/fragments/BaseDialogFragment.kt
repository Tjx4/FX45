package com.platform45.fx45.base.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.platform45.fx45.MyDrawerController
import com.platform45.fx45.R
import com.platform45.fx45.constants.LAYOUT

abstract class BaseDialogFragment : DialogFragment() {
    protected lateinit var myDrawerController: MyDrawerController
    protected var clickedView: View? = null
    protected var activity: AppCompatActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity?
        myDrawerController = activity as MyDrawerController
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
        //dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dialog?.window?.setDimAmount(0.9f)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.attributes?.let {
            it.width = WindowManager.LayoutParams.MATCH_PARENT
            it.height = WindowManager.LayoutParams.MATCH_PARENT
            dialog?.window?.attributes = it
        }
    }

    protected fun setViewClickEvents(views: Array<View>) {
        for (view in views) {
            if (view == null)
                continue

            view.setOnClickListener { v -> onFragmentViewClickedEvent(v) }
        }
    }

    open fun hideLoaderAndShowEnterMessage() {

    }


    protected fun setListviewClickEvents(listView: ListView) {
        listView.setOnItemClickListener { parent, view, position, id ->
            onFragmentViewClickedEvent(view)
        }
    }

    protected fun onFragmentViewClickedEvent(view: View) {
        dialog?.dismiss()
    }

}
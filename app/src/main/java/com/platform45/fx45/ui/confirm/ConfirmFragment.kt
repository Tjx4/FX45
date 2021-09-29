package com.platform45.fx45.ui.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.platform45.fx45.R
import com.platform45.fx45.base.fragments.BaseDialogFragment
import com.platform45.fx45.databinding.FragmentConfimBinding
import com.platform45.fx45.helpers.showDateTimeDialogFragment
import com.platform45.fx45.ui.dashboard.datetime.DateTimePickerFragment
import kotlinx.android.synthetic.main.fragment_confim.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmFragment: BaseDialogFragment(), DateTimePickerFragment.DateTimeSetter  {
    private lateinit var binding: FragmentConfimBinding
    private val conFirmViewModel: ConFirmViewModel by viewModel()
    override var dtIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confim, container, false)
        binding.lifecycleOwner = this
        binding.conFirmViewModel = conFirmViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        spnFrmCurrency.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                conFirmViewModel.setCurrencyPair(position, spnToCurrency.selectedItemPosition)
            }
        }

        spnToCurrency.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                conFirmViewModel.setCurrencyPair(spnFrmCurrency.selectedItemPosition, position)
            }
        }

        btnFrom.setOnClickListener {
            dtIndex = 0
            showDateTimeDialogFragment(this, (it as Button).text.toString())
        }

        btnTo.setOnClickListener {
            dtIndex = 1
            showDateTimeDialogFragment(this, (it as Button).text.toString())
        }
    }

    override fun setDate(year: Int, month: Int, day: Int) {
        when (dtIndex) {
            0 -> conFirmViewModel.setStartDate("$year-$month-$day")
            1 -> conFirmViewModel.setEndDate("$year-$month-$day")
        }
    }

    override fun setTime(scheduledTime: String) {
        when (dtIndex) {
            0 -> conFirmViewModel.setStartTime("$scheduledTime")
            1 -> conFirmViewModel.setEndTime("$scheduledTime")
        }
    }

    companion object {
        fun newInstance(): BaseDialogFragment {
            val bundle = Bundle()
            val confirmFragment = ConfirmFragment()
            confirmFragment.arguments = bundle
            return confirmFragment
        }
    }

}
package com.platform45.fx45.ui.confirm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.platform45.fx45.R
import com.platform45.fx45.adapters.CurrencyPairAdapter
import com.platform45.fx45.base.fragments.BaseDialogFragment
import com.platform45.fx45.base.fragments.BaseFragment
import com.platform45.fx45.databinding.FragmentConfimBinding
import com.platform45.fx45.extensions.getScreenCols
import com.platform45.fx45.helpers.showDateTimeDialogFragment
import com.platform45.fx45.ui.convert.ConversionFragmentArgs
import com.platform45.fx45.ui.dashboard.DashboardFragmentDirections
import com.platform45.fx45.ui.dashboard.datetime.DateTimePickerFragment
import kotlinx.android.synthetic.main.fragment_confim.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmFragment: BaseDialogFragment(), DateTimePickerFragment.DateTimeSetter, CurrencyPairAdapter.UserInteractions  {
    private lateinit var binding: FragmentConfimBinding
    private val conFirmViewModel: ConFirmViewModel by viewModel()
    private val args: ConfirmFragmentArgs by navArgs()
    override var dtIndex: Int = 0

    override fun onDetach() {
        super.onDetach()
        //myDrawerController.showActionBarIcon()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //myDrawerController.showSelectionMode()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confim, container, false)
        binding.lifecycleOwner = this
        binding.conFirmViewModel = conFirmViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addObservers()
        conFirmViewModel.setPairsList(args.currencyPairs)

        btnCloseDialog.setOnClickListener {
            dismiss()
        }

        btnGetHistory.setOnClickListener {
            //myDrawerController.hideActionBarIcon()
            val startDate = conFirmViewModel.startDate.value ?: ""
            val endDate = conFirmViewModel.endDate.value ?: ""
            val currencyPairs = conFirmViewModel.getCurrencyPairsString()
            val action = ConfirmFragmentDirections.confirmToTradeHistory(startDate, endDate, currencyPairs)
            findNavController().navigate(action)
        }

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

    private fun addObservers() {
        conFirmViewModel.selectedPairs.observe(viewLifecycleOwner, { onCurrencyPairsSet(it)})
        conFirmViewModel.isPairsUpdated.observe(viewLifecycleOwner, { onPairsListUpdated(it)})
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

    override fun onPairClicked(view: View, position: Int) {
    }

    override fun onDeleteClicked(pair: String, position: Int) {
        conFirmViewModel.removePairFromList(pair)
        Toast.makeText(context, "$pair deleted", Toast.LENGTH_SHORT).show()
    }

    private fun onPairsListUpdated(isUpdated: Boolean){
        rvPorpularCp?.adapter?.notifyDataSetChanged()
        rvRequestingPairs?.adapter?.notifyDataSetChanged()
    }

    private fun onCurrencyPairsSet(pairs: List<String>) {
        val pairsAdapter = CurrencyPairAdapter(requireContext(), pairs)
        pairsAdapter.setPairClickListener(this)
        val cols = requireActivity().getScreenCols(125f)
        val requestingPairsManager = GridLayoutManager(context, cols)
        rvRequestingPairs?.adapter = pairsAdapter
        rvRequestingPairs?.layoutManager = requestingPairsManager
    }

    companion object {
        fun newInstance(): ConfirmFragment {
            val bundle = Bundle()
            val confirmFragment = ConfirmFragment()
            confirmFragment.arguments = bundle
            return confirmFragment
        }
    }

}
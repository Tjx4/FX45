package com.platform45.fx45.ui.convert

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.platform45.fx45.R
import com.platform45.fx45.base.fragments.BaseFragment
import com.platform45.fx45.databinding.FragmentConversionBinding
import xyz.appic.common.helpers.showErrorDialog
import kotlinx.android.synthetic.main.fragment_conversion.*
import kotlinx.coroutines.Dispatchers

class ConversionFragment : BaseFragment() {
    private lateinit var binding: FragmentConversionBinding
    private val conversionViewModel: ConversionViewModel by viewModel()
    private val args: ConversionFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController.setTitle(getString(R.string.convert_currencies))
    }

    override fun onDetach() {
        super.onDetach()
        myDrawerController.showActionBarIcon()
        myDrawerController.showMenu()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        myDrawerController.hideMenu()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversion, container, false)
        binding.lifecycleOwner = this
        binding.conversionViewModel = conversionViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        conversionViewModel.presetCurrencyPair(args.fromCurrency, args.toCurrency)
        addObservers()
    }

    private fun addObservers() {
        conversionViewModel.conversion.observe(viewLifecycleOwner, { onShowConversion() })
        conversionViewModel.showLoading.observe(viewLifecycleOwner, { onShowLoader() })
        conversionViewModel.error.observe(viewLifecycleOwner, { onShowErrorMessage() })
        conversionViewModel.dialogErrorMessage.observe(viewLifecycleOwner, { onShowErrorDialog(it) })
        conversionViewModel.isValidInput.observe(viewLifecycleOwner, { onValidInputAdded() })
    }

    private fun onShowConversion(){
        cnvLoader.visibility = View.INVISIBLE
        tvTotal.visibility = View.VISIBLE
        tvError.visibility = View.INVISIBLE
    }

    private fun onShowLoader(){
        cnvLoader.visibility = View.VISIBLE
        tvTotal.visibility = View.INVISIBLE
        tvError.visibility = View.INVISIBLE
    }

    private fun onShowErrorMessage(){
        cnvLoader.visibility = View.INVISIBLE
        tvTotal.visibility = View.INVISIBLE
        tvError.visibility = View.VISIBLE
    }

    private fun onShowErrorDialog(errorMessage: String){
        cnvLoader.visibility = View.INVISIBLE
        tvTotal.visibility = View.INVISIBLE
        tvError.visibility = View.INVISIBLE
        showErrorDialog(requireContext(), getString(R.string.error), errorMessage, getString(R.string.close))
    }

    private fun onValidInputAdded(){
        conversionViewModel.let {
            it.viewModelScope.launch(Dispatchers.IO) {
                it.convertCurrency(it.from.value ?: "", it.to.value?: "", it.amount.value?: "")
            }
        }
    }
}
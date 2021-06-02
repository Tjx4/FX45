package com.platform45.fx45.ui.convert

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.platform45.fx45.R
import com.platform45.fx45.base.fragments.BaseFragment
import com.platform45.fx45.databinding.FragmentConversionBinding
import com.platform45.weather45.models.Conversion
import kotlinx.android.synthetic.main.fragment_conversion.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConversionFragment : BaseFragment() {
    private lateinit var binding: FragmentConversionBinding
    private val conversionViewModel: ConversionViewModel by viewModel()
    private val args: ConversionFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController.setTitle(getString(R.string.convert_currencies))
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
        Navigation.findNavController(view).currentDestination?.label = getString(R.string.convert_currencies)

        conversionViewModel.presetCurrencies(args.fromCurrency, args.toCurrency)
        addObservers()

        btnConvert.setOnClickListener {
            conversionViewModel.checkAndConvert()
        }
    }

    private fun addObservers() {
        conversionViewModel.convert.observe(viewLifecycleOwner, Observer { onConversion(it) })
        conversionViewModel.showLoading.observe(viewLifecycleOwner, Observer { onShowLoading(it)})
    }

    private fun onConversion(conversion: Conversion?){
        cnvLoader.visibility = View.GONE
        tvTotal.visibility = View.VISIBLE
    }

    private fun onShowLoading(showLoading: Boolean){
        cnvLoader.visibility = View.VISIBLE
    }
}
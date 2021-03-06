package com.platform45.fx45.ui.convert

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.platform45.fx45.R
import com.platform45.fx45.base.fragments.BaseFragment
import com.platform45.fx45.databinding.FragmentConversionBinding
import com.platform45.fx45.models.Conversion
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

    override fun onDetach() {
        super.onDetach()
        myDrawerController.showActionBarIcon()
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
        conversionViewModel.convert.observe(viewLifecycleOwner, { onConversion(it) })
        conversionViewModel.showLoading.observe(viewLifecycleOwner, { onShowLoading(it) })
        conversionViewModel.error.observe(viewLifecycleOwner, { onError(it) })
    }

    private fun onConversion(conversion: Conversion?){
        cnvLoader.visibility = View.INVISIBLE
        tvTotal.visibility = View.VISIBLE
        tvError.visibility = View.INVISIBLE
    }

    private fun onShowLoading(showLoading: Boolean){
        cnvLoader.visibility = View.VISIBLE
        tvTotal.visibility = View.INVISIBLE
        tvError.visibility = View.INVISIBLE
    }

    private fun onError(error: String){
        cnvLoader.visibility = View.INVISIBLE
        tvTotal.visibility = View.INVISIBLE
        tvError.visibility = View.VISIBLE
    }
}
package com.platform45.fx45.ui.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.platform45.fx45.R
import com.platform45.fx45.base.fragments.BaseFragment
import com.platform45.fx45.helpers.showErrorDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val dashboardViewModel: HistoryViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController.setTitle(getString(R.string.trade_history))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myDrawerController.hideMenu()
        binding = DataBindingUtil.inflate(inflater, R.layout.history_fragment, container, false)
        binding.lifecycleOwner = this
        binding.dashboardViewModel = dashboardViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigation.findNavController(view).currentDestination?.label = getString(R.string.trade_history)

    }

    private fun addObservers() {
        dashboardViewModel.showLoading.observe(viewLifecycleOwner, Observer { onShowLoading(it) })
    }

    private fun onShowLoading(showLoading: Boolean){
        showLoading()
    }

    fun showError(errorMessage: String){
        showErrorDialog(requireContext(), getString(R.string.error), errorMessage, getString(R.string.close))
        myDrawerController.hideLoading()
    }

    fun showLoading(){
        myDrawerController.showLoading()
    }

}
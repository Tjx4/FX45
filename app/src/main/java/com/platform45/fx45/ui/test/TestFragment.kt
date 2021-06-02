package com.platform45.fx45.ui.test

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.platform45.fx45.R
import com.platform45.fx45.adapters.HistoryLoadStateAdapter
import com.platform45.fx45.adapters.HistoryPagingAdapter
import com.platform45.fx45.base.fragments.BaseFragment
import com.platform45.fx45.databinding.FragmentTestBinding
import com.platform45.fx45.helpers.showErrorDialog
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.history_fragment.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestFragment : BaseFragment() {
    private lateinit var binding: FragmentTestBinding
    private val historyViewModel: HistoryViewModel by viewModel()
    private lateinit var historyPagingAdapter: HistoryPagingAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController.setTitle(getString(R.string.trade_history))
        historyPagingAdapter = HistoryPagingAdapter(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myDrawerController.hideMenu()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        binding.lifecycleOwner = this
        binding.historyViewModel = historyViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigation.findNavController(view).currentDestination?.label = getString(R.string.trade_history)

        addObservers()
        initRecyclerView()
    }

    fun initRecyclerView(){
        rvtrades.apply {
            rvPorpularCp?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = historyPagingAdapter.withLoadStateFooter(
                footer = HistoryLoadStateAdapter(historyPagingAdapter)
            )
        }

        lifecycleScope.launch {
            historyViewModel.popularCurrencyPairs.collectLatest {
                historyPagingAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            historyPagingAdapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Error -> {
                        val error = when {
                            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                            else -> null
                        }
                        error?.let {
                            val message = if(it.error.message.isNullOrEmpty()) getString(R.string.no_data_found) else it.error.message!!
                            showError(message)
                        }
                    }
                    is LoadState.Loading -> myDrawerController.showLoading()
                    is LoadState.NotLoading -> myDrawerController.hideLoading()
                }
            }
        }
    }

    private fun addObservers() {
        historyViewModel.showLoading.observe(viewLifecycleOwner, Observer { onShowLoading(it) })
    }

    private fun onShowLoading(showLoading: Boolean){
        myDrawerController.showLoading()
    }

    fun showError(errorMessage: String){
        showErrorDialog(requireContext(), getString(R.string.error), errorMessage, getString(R.string.close))
        myDrawerController.hideLoading()
    }


}
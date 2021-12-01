package com.platform45.fx45.ui.tradeHistory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.platform45.fx45.R
import com.platform45.fx45.adapters.HistoryLoadStateAdapter
import com.platform45.fx45.adapters.HistoryPagingAdapter
import com.platform45.fx45.base.fragments.BaseFragment
import com.platform45.fx45.databinding.FragmentTradeHistoryBinding
import xyz.appic.common.helpers.showErrorDialog
import kotlinx.android.synthetic.main.fragment_trade_history.*
import kotlinx.coroutines.flow.collectLatest

class TradeHistoryFragment : BaseFragment() {
    private lateinit var binding: FragmentTradeHistoryBinding
    private val tradeHistoryViewModel: TradeHistoryViewModel by viewModel()
    private lateinit var historyPagingAdapter: HistoryPagingAdapter
    private val args: TradeHistoryFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController.setTitle(getString(R.string.trade_history))
        historyPagingAdapter = HistoryPagingAdapter(context)
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trade_history, container, false)
        binding.lifecycleOwner = this
        binding.tradeHistoryViewModel = tradeHistoryViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Navigation.findNavController(view).currentDestination?.label = getString(R.string.trade_history)

        tradeHistoryViewModel.setParams(args.startDate, args.endDate, args.currencyPairs)
        tradeHistoryViewModel.setPairsList(args.currencyPairs)
        addObservers()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        rvtrades.apply {
            rvtrades?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = historyPagingAdapter.withLoadStateFooter(
                footer = HistoryLoadStateAdapter(historyPagingAdapter)
            )
        }

        lifecycleScope.launch {
            tradeHistoryViewModel.popularCurrencyPairs.collectLatest {
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
                    is LoadState.NotLoading -> showHistory()
                }
            }
        }
    }

    private fun addObservers() {
        tradeHistoryViewModel.showLoading.observe(viewLifecycleOwner, Observer { onShowLoading(it) })
        tradeHistoryViewModel.selectedPairs.observe(viewLifecycleOwner, Observer { onPairsListSet(it) })
    }

    private fun onShowLoading(showLoading: Boolean){
        myDrawerController.showLoading()
    }

    private fun onPairsListSet(pairs: List<String>){
        for((indx, pair) in pairs.withIndex()){
            tbCategories.addTab(tbCategories.newTab().setTag(indx).setText(pair))
        }
        tbCategories.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val indx = tab.tag.toString().toInt()
                rvtrades.scrollToPosition(indx)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    fun showError(errorMessage: String){
        showErrorDialog(requireContext(), getString(R.string.error), errorMessage, getString(R.string.close))
        myDrawerController.hideLoading()
    }

    fun showHistory(){
        myDrawerController.hideLoading()
        tvNoContent.visibility = View.GONE
    }

}
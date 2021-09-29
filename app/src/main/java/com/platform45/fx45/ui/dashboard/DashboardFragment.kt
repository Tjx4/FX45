package com.platform45.fx45.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.platform45.fx45.R
import com.platform45.fx45.adapters.PPLoadStateAdapter
import com.platform45.fx45.adapters.PopularPairsPagingAdapter
import com.platform45.fx45.base.fragments.BaseFragment
import com.platform45.fx45.databinding.FragmentDashboardBinding
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.platform45.fx45.adapters.CurrencyPairAdapter
import com.platform45.fx45.extensions.getScreenCols
import com.platform45.fx45.extensions.splitInTwo
import com.platform45.fx45.helpers.showDateTimeDialogFragment
import com.platform45.fx45.helpers.showErrorDialog

class DashboardFragment : BaseFragment(), PopularPairsPagingAdapter.AddPairClickListener, CurrencyPairAdapter.UserInteractions {
    private lateinit var binding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private lateinit var popularPairsPagingAdapter: PopularPairsPagingAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController.setDashboardFragment(this)
        myDrawerController.setTitle(getString(R.string.forty_five))
        popularPairsPagingAdapter = PopularPairsPagingAdapter(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        myDrawerController.showMenu()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.lifecycleOwner = this
        binding.dashboardViewModel = dashboardViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigation.findNavController(view).currentDestination?.label = getString(R.string.forty_five)
        dashboardViewModel.checkState()
        addObservers()
        initRecyclerView()


        btnAddCurrencyPair.setOnClickListener {
            dashboardViewModel.addCreatedPairToList()
        }

        btnRequestHistory.setOnClickListener {
            showPairSelector()
        }

        btnGetHistory.setOnClickListener {
            val startDate = dashboardViewModel.startDate.value ?: ""
            val endDate = dashboardViewModel.endDate.value ?: ""
            val currencyPairs = dashboardViewModel.getCurrencyPairsString()
            myDrawerController.hideActionBarIcon()
            val action = DashboardFragmentDirections.dashboardToTradeHistory(startDate, endDate, currencyPairs)
            findNavController().navigate(action)
        }
    }

    fun initRecyclerView(){
        popularPairsPagingAdapter.dashboardViewModel = dashboardViewModel
        rvPorpularCp.apply {
            rvPorpularCp?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = popularPairsPagingAdapter.withLoadStateFooter(
                footer = PPLoadStateAdapter(popularPairsPagingAdapter)
            )
        }

        popularPairsPagingAdapter.addPairClickListener(this)

        lifecycleScope.launch {
            dashboardViewModel.popularCurrencyPairs.collectLatest {
                popularPairsPagingAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            popularPairsPagingAdapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Error -> {
                        val error = when {
                            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                            else -> null
                        }
                        error?.let {
                            val message =
                                if (it.error.message.isNullOrEmpty()) getString(R.string.no_popular_currency_pairs) else it.error.message!!
                            showError(message)
                        }
                    }
                    is LoadState.Loading -> myDrawerController.showLoading()
                    is LoadState.NotLoading -> showPairSeriesInfo()
                }
            }
        }

    }

    private fun addObservers() {
        dashboardViewModel.isPairsUpdated.observe(viewLifecycleOwner, { onPairsListUpdated(it)})
        dashboardViewModel.canProceed.observe(viewLifecycleOwner, { canProceed(it)})
        dashboardViewModel.currencyPairs.observe(viewLifecycleOwner, { onCurrencyPairsSet(it)})
    }

    override fun onPairClicked(position: Int, pair: String) {
        dashboardViewModel.togglePopularPairFromList(pair)
    }

    override fun onConvertClicked(pair: String) {
        val fromCurrency = pair.splitInTwo()[0]
        val toCurrency = pair.splitInTwo()[1]
        val action = DashboardFragmentDirections.dashboardToConversion(fromCurrency, toCurrency)
        myDrawerController.hideActionBarIcon()
        findNavController().navigate(action)
    }

    override fun onPairClicked(view: View, position: Int) {
    }

    override fun onDeleteClicked(pair: String, position: Int) {
        dashboardViewModel.removePairFromList(position)
        Toast.makeText(context, "$pair deleted", Toast.LENGTH_SHORT).show()
    }

    private fun showError(errorMessage: String){
        showErrorDialog(requireContext(), getString(R.string.error), errorMessage, getString(R.string.close))
        myDrawerController.hideLoading()
    }

    fun refresh() {
        popularPairsPagingAdapter.refresh()
    }

    private fun showPairSelector(){
        clPairSelector.visibility = View.VISIBLE
        clPairSeriesInfo.visibility = View.INVISIBLE
        myDrawerController.showSelectionMode()
    }

    fun showPairSeriesInfo() {
        clPairSelector.visibility = View.INVISIBLE
        clPairSeriesInfo.visibility = View.VISIBLE
        myDrawerController.showContent()
    }

    private fun canProceed(proceed: Boolean){
        btnGetHistory.isEnabled = proceed
        btnGetHistory.background = resources.getDrawable( if(proceed) R.drawable.fx_button_background  else R.drawable.fx_disabled_button_background)
        tvRequestingPairs.visibility = if(proceed) View.VISIBLE else View.INVISIBLE
        btnRequestHistory.visibility = if(proceed) View.VISIBLE else View.INVISIBLE
    }



    private fun onCurrencyPairsSet(pairs: List<String>) {
        val pairsAdapter = CurrencyPairAdapter(requireContext(), pairs)
        pairsAdapter.setPairClickListener(this)
        val cols = requireActivity().getScreenCols(125f)
        val requestingPairsManager = GridLayoutManager(context, cols)
        rvRequestingPairs?.adapter = pairsAdapter
        rvRequestingPairs?.layoutManager = requestingPairsManager
    }

    private fun onPairsListUpdated(isUpdated: Boolean){
        rvPorpularCp?.adapter?.notifyDataSetChanged()
        rvRequestingPairs?.adapter?.notifyDataSetChanged()
    }
}
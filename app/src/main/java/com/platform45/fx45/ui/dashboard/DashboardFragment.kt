package com.platform45.fx45.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.platform45.fx45.extensions.splitInTwo
import com.platform45.fx45.helpers.showErrorDialog

class DashboardFragment : BaseFragment(), PopularPairsPagingAdapter.AddPairClickListener {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.lifecycleOwner = this
        binding.dashboardViewModel = dashboardViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigation.findNavController(view).currentDestination?.label = getString(R.string.forty_five)

        addObservers()
        initRecyclerView()
        btnRequestHistory.setOnClickListener { goToConfirmScreen() }
    }

    private fun addObservers() {
        dashboardViewModel.canProceed.observe(viewLifecycleOwner, { btnRequestHistory.visibility = View.VISIBLE })
        dashboardViewModel.hideProceed.observe(viewLifecycleOwner, { btnRequestHistory.visibility = View.INVISIBLE })
        dashboardViewModel.selectedPairMessage.observe(viewLifecycleOwner, { onPairSelected(it)})
    }

    private fun initRecyclerView(){
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
                            val message = if (it.error.message.isNullOrEmpty()) getString(R.string.no_popular_currency_pairs) else it.error.message!!
                            showError(message)
                        }
                    }
                    is LoadState.Loading -> myDrawerController.showLoading()
                    is LoadState.NotLoading -> showPairSeriesInfo()
                }
            }
        }
    }

    override fun onConvertClicked(pair: String) {
        val fromCurrency = pair.splitInTwo()[0]
        val toCurrency = pair.splitInTwo()[1]
        myDrawerController.hideActionBarIcon()
        val action = DashboardFragmentDirections.dashboardToConversion(fromCurrency, toCurrency)
        findNavController().navigate(action)
    }

    private fun goToConfirmScreen(){
        val currencyPairs = dashboardViewModel.getCurrencyPairsString()
        val action = DashboardFragmentDirections.dashboardToConfirm(currencyPairs)
        findNavController().navigate(action)
    }

    override fun onPairClicked(position: Int, pair: String) {
        dashboardViewModel.togglePopularPairFromList(pair)
        dashboardViewModel.toggleStatus()
    }

    private fun showError(errorMessage: String){
        showErrorDialog(requireContext(), getString(R.string.error), errorMessage, getString(R.string.close))
        myDrawerController.hideLoading()
    }

    fun refresh() {
        popularPairsPagingAdapter.refresh()
    }

    fun showPairSeriesInfo() {
        myDrawerController.showContent()
    }

    private fun onPairSelected(message: String){
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}
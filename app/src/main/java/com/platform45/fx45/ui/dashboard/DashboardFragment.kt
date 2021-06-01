package com.platform45.fx45.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.platform45.fx45.R
import com.platform45.fx45.adapters.PPLoadStateAdapter
import com.platform45.fx45.adapters.PopularPairsAdapter
import com.platform45.fx45.base.fragments.BaseFragment
import com.platform45.fx45.databinding.FragmentDashboardBinding
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.platform45.fx45.helpers.showErrorDialog
import kotlinx.android.synthetic.main.history_fragment.*

class DashboardFragment : BaseFragment(), PopularPairsAdapter.AddPairClickListener {
    private lateinit var binding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var popularPairsAdapter: PopularPairsAdapter =  PopularPairsAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController.setDashboardFragment(this)
        myDrawerController.setTitle(getString(R.string.app_name))
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
        Navigation.findNavController(view).currentDestination?.label = getString(R.string.app_name)

        dashboardViewModel.checkState()
        addObservers()

        rvPorpularCp.apply {
            rvPorpularCp?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = popularPairsAdapter.withLoadStateFooter(
                footer =  PPLoadStateAdapter(popularPairsAdapter)
            )
        }

        popularPairsAdapter.addPairClickListener(this)

        lifecycleScope.launch {
            dashboardViewModel.popularCurrencyPairs.collectLatest {
                popularPairsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            popularPairsAdapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Error -> {
                        val error = when {
                            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                            else -> null
                        }
                        error?.let {
                            val message = if(it.error.message.isNullOrEmpty()) getString(R.string.no_popular_currency_pairs) else it.error.message!!
                            showError(message)
                        }
                    }
                    is LoadState.Loading ->  showLoading()
                    is LoadState.NotLoading -> showPairSeriesInfo()
                }
            }
        }

    }

    private fun addObservers() {
        dashboardViewModel.isPairsUpdated.observe(viewLifecycleOwner, Observer { onPairsListUpdated(it)})
    }

    override fun onPairClicked(position: Int) {

    }

    override fun onConvertClicked(pair: String) {

    }

    fun showError(errorMessage: String){
        flLoader.visibility = View.GONE
        showErrorDialog(requireContext(), getString(R.string.error), errorMessage, getString(R.string.close))
    }

    fun showLoading(){
        myDrawerController.hideToolbar()
        flLoader.visibility = View.VISIBLE
    }

    fun showPairSelector(){
        flLoader.visibility = View.GONE
        clPairSelector.visibility = View.VISIBLE
        clPairSeriesInfo.visibility = View.GONE
        myDrawerController.showSelectionMode()
    }

    fun showPairSeriesInfo() {
        flLoader.visibility = View.GONE
        clPairSelector.visibility = View.GONE
        clPairSeriesInfo.visibility = View.VISIBLE
        myDrawerController.showContent()
    }

    private fun canProceed(proceed: Boolean){
        btnGetHistory.isEnabled = proceed
        btnGetHistory.background = resources.getDrawable( if(proceed) R.drawable.fx_button_background  else R.drawable.fx_disabled_button_background)
        tvRequestingPairs.visibility = if(proceed) View.VISIBLE else View.GONE
    }

    override fun setDate(year: Int, month: Int, day: Int) {
        when (indx) {
            0 -> btnFrom.text = "$year-$month-$day" //Todo fix
            1 -> btnTo.text = "$year-$month-$day"
        }
    }

    override fun setTime(scheduledTime: String) {
        when (indx) {
            0 -> btnFrom.text = "${btnFrom.text}-$scheduledTime"
            1 -> btnTo.text = "${btnTo.text}-$scheduledTime"
        }
    }

    fun onPairsListUpdated(isUpdated: Boolean){
        rvRequestingPairs?.adapter?.notifyDataSetChanged()
    }

}
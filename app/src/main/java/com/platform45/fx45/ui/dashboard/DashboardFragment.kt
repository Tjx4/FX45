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
import androidx.lifecycle.Observer
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
import com.platform45.fx45.helpers.showDateTimeDialogFragment
import com.platform45.fx45.helpers.showErrorDialog
import com.platform45.fx45.ui.dashboard.datetime.DateTimePickerFragment

class DashboardFragment : BaseFragment(), PopularPairsAdapter.AddPairClickListener, DateTimePickerFragment.DateTimeSetter {
    private lateinit var binding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private lateinit var popularPairsAdapter: PopularPairsAdapter
    override var indx: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController.setDashboardFragment(this)
        myDrawerController.setTitle(getString(R.string.app_name))
        popularPairsAdapter = PopularPairsAdapter(context)
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
        initRecyclerView()

        spnFrmCurrency.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dashboardViewModel.setCurrencyPair(position, spnToCurrency.selectedItemPosition)
            }
        }

        spnToCurrency.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dashboardViewModel.setCurrencyPair(spnFrmCurrency.selectedItemPosition, position)
            }
        }

        btnFrom.setOnClickListener {
            indx = 0
            showDateTimeDialogFragment(this, (it as Button).text.toString())
        }

        btnTo.setOnClickListener {
            indx = 1
            showDateTimeDialogFragment(this, (it as Button).text.toString())
        }

        btnRequestHistory.setOnClickListener {
            showPairSelector()
        }

        btnGetHistory.setOnClickListener {
            //view history
        }
    }

    fun initRecyclerView(){
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
        dashboardViewModel.canProceed.observe(viewLifecycleOwner, Observer { canProceed(it)})
    }

    override fun onPairClicked(position: Int, pair: String) {
        dashboardViewModel.addPopularPairToList(pair)
        Toast.makeText(context, "$pair selected", Toast.LENGTH_SHORT).show()
    }

    override fun onConvertClicked(pair: String) {
        //Go to convert
        Toast.makeText(context, "$pair convert", Toast.LENGTH_SHORT).show()
    }

    private fun showError(errorMessage: String){
        flLoader.visibility = View.INVISIBLE
        showErrorDialog(requireContext(), getString(R.string.error), errorMessage, getString(R.string.close))
    }

    private fun showLoading(){
        myDrawerController.hideToolbar()
        flLoader.visibility = View.VISIBLE
    }

    fun showPairSelector(){
        flLoader.visibility = View.INVISIBLE
        clPairSelector.visibility = View.VISIBLE
        rvPorpularCp.visibility = View.INVISIBLE
        myDrawerController.showSelectionMode()
    }

    fun showPairSeriesInfo() {
        flLoader.visibility = View.INVISIBLE
        clPairSelector.visibility = View.INVISIBLE
        rvPorpularCp.visibility = View.VISIBLE
        myDrawerController.showContent()
    }

    private fun canProceed(proceed: Boolean){
        btnGetHistory.isEnabled = proceed
        btnGetHistory.background = resources.getDrawable( if(proceed) R.drawable.fx_button_background  else R.drawable.fx_disabled_button_background)
        tvRequestingPairs.visibility = if(proceed) View.VISIBLE else View.INVISIBLE
        btnRequestHistory.visibility = if(proceed) View.VISIBLE else View.INVISIBLE
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
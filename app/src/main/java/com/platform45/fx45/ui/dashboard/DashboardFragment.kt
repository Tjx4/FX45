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

class DashboardFragment : BaseFragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var popularPairsAdapter: PopularPairsAdapter =  PopularPairsAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //myDrawerController.showMenu()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.lifecycleOwner = this
        binding.dashboardViewModel = dashboardViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigation.findNavController(view).currentDestination?.label = getString(R.string.app_name)
        addObservers()

        rvPorpularCp.apply {
            rvPorpularCp?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
            adapter = popularPairsAdapter.withLoadStateFooter(
                footer =  PPLoadStateAdapter(popularPairsAdapter)
            )
        }

        lifecycleScope.launch {
            dashboardViewModel.popularCurrencyPairs.collectLatest {
                popularPairsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            popularPairsAdapter.loadStateFlow.collectLatest { loadState ->
                if (loadState.refresh is LoadState.Loading){

                }
                else{
                 val dfdf = loadState
                }
            }
        }

    }

    private fun addObservers() {
        //dashboardViewModel.showLoading.observe(viewLifecycleOwner, Observer { onShowLoading(it)})
    }

}
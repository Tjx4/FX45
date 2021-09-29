package com.platform45.fx45.ui.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.platform45.fx45.R
import com.platform45.fx45.base.fragments.BaseDialogFragment
import com.platform45.fx45.databinding.FragmentConfimBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmFragment: BaseDialogFragment()  {
    private lateinit var binding: FragmentConfimBinding
    private val conFirmViewModel: ConFirmViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confim, container, false)
        binding.lifecycleOwner = this
        binding.conFirmViewModel = conFirmViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {
        fun newInstance(): BaseDialogFragment {
            val bundle = Bundle()
            val confirmFragment = ConfirmFragment()
            confirmFragment.arguments = bundle
            return confirmFragment
        }
    }

}
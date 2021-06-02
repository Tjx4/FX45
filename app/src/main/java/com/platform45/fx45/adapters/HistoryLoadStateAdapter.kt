package com.platform45.fx45.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.platform45.fx45.R
import com.wang.avi.AVLoadingIndicatorView

class HistoryLoadStateAdapter(
    private val pagingAdapter: HistoryPagingAdapter
) : LoadStateAdapter<HistoryLoadStateAdapter.NetworkStateItemViewHolder>() {

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateItemViewHolder {
        return NetworkStateItemViewHolder(parent) { pagingAdapter.retry() }
    }

    inner class NetworkStateItemViewHolder internal constructor(parent: ViewGroup, private val retryCallback: () -> Unit) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.load_state_layout, parent, false)){
        private val progressBar: AVLoadingIndicatorView = itemView.findViewById(R.id.pb_progress)
        private val retry: Button = itemView.findViewById(R.id.b_retry)

        init {
            retry.setOnClickListener { retryCallback() }
        }

        fun bindTo(loadState: LoadState) {
            progressBar.isVisible = loadState is LoadState.Loading
            retry.isVisible = loadState is LoadState.Error
        }
    }

}
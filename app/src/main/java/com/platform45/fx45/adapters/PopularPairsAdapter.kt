package com.platform45.fx45.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.platform45.fx45.R
import com.platform45.fx45.persistance.room.tables.popularPair.PopularPairTable

class PopularPairsAdapter() : PagingDataAdapter<PopularPairTable, PopularPairsAdapter.PopularViewHolder>(PairComparator)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.cp_layout,
            parent,
            false
        )
        return PopularViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val currentPair = getItem(position)
        holder.favCpTv.text = currentPair?.pair
    }

    inner class PopularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var favCpTv = itemView.findViewById<TextView>(R.id.tvFavCp)
    }
}

object PairComparator : DiffUtil.ItemCallback<PopularPairTable>() {
    override fun areItemsTheSame(oldItem: PopularPairTable, newItem: PopularPairTable) = oldItem.pair == newItem.pair
    override fun areContentsTheSame(oldItem: PopularPairTable, newItem: PopularPairTable) = oldItem == newItem
}
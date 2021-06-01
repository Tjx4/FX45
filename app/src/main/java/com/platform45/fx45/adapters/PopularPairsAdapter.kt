package com.platform45.fx45.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.platform45.fx45.R
import com.platform45.fx45.persistance.room.tables.popularPair.PopularPairTable

class PopularPairsAdapter : PagingDataAdapter<PopularPairTable, PopularPairsAdapter.PopularViewHolder>(PairComparator)  {

    private var pairClickListener: AddPairClickListener? = null

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
        holder.favCpFnTv.text = currentPair?.fullName
        holder.convertImgb.setOnClickListener {
            currentPair?.pair?.let { it1 -> pairClickListener?.onConvertClicked(it1) }
        }
    }

    inner class PopularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var favCpTv = itemView.findViewById<TextView>(R.id.tvFavCp)
        internal var favCpFnTv = itemView.findViewById<TextView>(R.id.tvFavCpFn)
        internal var convertImgb = itemView.findViewById<ImageButton>(R.id.btnConvert)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            pairClickListener?.onPairClicked(adapterPosition)
        }
    }

    interface AddPairClickListener {
        fun onPairClicked(position: Int)
        fun onConvertClicked(pair: String)
    }

    fun addPairClickListener(pairClickListener: AddPairClickListener) {
        this.pairClickListener = pairClickListener
    }

}

object PairComparator : DiffUtil.ItemCallback<PopularPairTable>() {
    override fun areItemsTheSame(oldItem: PopularPairTable, newItem: PopularPairTable) = oldItem.pair == newItem.pair
    override fun areContentsTheSame(oldItem: PopularPairTable, newItem: PopularPairTable) = oldItem == newItem
}
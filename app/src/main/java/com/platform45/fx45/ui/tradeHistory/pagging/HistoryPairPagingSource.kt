package com.platform45.fx45.ui.tradeHistory.pagging

import androidx.paging.PagingSource
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.helpers.getPairHistoryList
import com.platform45.fx45.helpers.toDbTable
import com.platform45.fx45.helpers.toPricseLinkedTreeMap
import com.platform45.fx45.persistance.room.tables.pairHistory.PairHistoryTable
import com.platform45.fx45.repositories.FXRepository

class HistoryPairPagingSource(private val startDate: String, private val endDate: String, private val currency: String, private val fXRepository: FXRepository) : PagingSource<Int, PairHistoryTable>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PairHistoryTable> = try {
        val loadPage = params.key ?: 0

        val result = fXRepository.getSeries(
            API_KEY,
            startDate,
            endDate,
            currency,
            "ohlc")

        val response = ArrayList<PairHistoryTable>()
        val price = result?.price?.toPricseLinkedTreeMap()
        val currencyPairs = currency.split(",")
        val pairHistories = getPairHistoryList(startDate, endDate, currencyPairs, price)
        pairHistories?.forEach {
            it?.let { pairHistory -> response.add(pairHistory.toDbTable()) }
        }

        LoadResult.Page(
            data = response,
            prevKey = null,
            nextKey = loadPage + 1
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

}
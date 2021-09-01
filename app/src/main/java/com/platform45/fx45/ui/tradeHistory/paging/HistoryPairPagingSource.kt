package com.platform45.fx45.ui.tradeHistory.paging

import androidx.paging.PagingSource
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.constants.H_PAGE_SIZE
import com.platform45.fx45.helpers.getPairHistoryList
import com.platform45.fx45.helpers.toDbTable
import com.platform45.fx45.helpers.toPricseLinkedTreeMap
import com.platform45.fx45.persistance.room.tables.pairHistory.PairHistoryTable
import com.platform45.fx45.repositories.FXRepository
import com.platform45.fx45.repositories.IFXRepository
import java.lang.NullPointerException

class HistoryPairPagingSource(private val startDate: String, private val endDate: String, private val currency: String, private val fXRepository: IFXRepository) : PagingSource<Int, PairHistoryTable>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PairHistoryTable> = try {
        val loadPage = params.key ?: 0

        val result = fXRepository.getSeries(
            API_KEY,
            startDate,
            endDate,
            currency,
            "ohlc"
        )

        if (result == null || result?.error != null) {
            val error = result?.error
            LoadResult.Error(NullPointerException(error?.info ?: "Unknown error"))
        }
        else{
            val response = ArrayList<PairHistoryTable>()
            val price = result?.price?.toPricseLinkedTreeMap()
            val currencyPairs = currency.split(",")
            val pairHistories = getPairHistoryList(startDate, endDate, currencyPairs, price)
            pairHistories?.forEach {
                it?.let { pairHistory -> response.add(pairHistory.toDbTable()) }
            }

            val pages = response.size / H_PAGE_SIZE
            val currentPage = getCurrentPage(response, loadPage, H_PAGE_SIZE)

            LoadResult.Page(
                data = currentPage,
                prevKey = null,
                nextKey = if (loadPage < pages) loadPage + 1 else null
            )
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    private fun getCurrentPage(response: List<PairHistoryTable>, pageIndex: Int, pageSize: Int): List<PairHistoryTable>{
        val pageData = response.withIndex().groupBy { it.index / pageSize }.values.map { ph -> ph.map { it.value } }
        return pageData[pageIndex]
    }

}
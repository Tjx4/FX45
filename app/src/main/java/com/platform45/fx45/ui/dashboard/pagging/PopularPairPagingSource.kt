package com.platform45.fx45.ui.dashboard.pagging

import androidx.paging.PagingSource
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.persistance.room.tables.popularPair.PopularPairTable
import com.platform45.fx45.repositories.FXRepository

class PopularPairPagingSource(private val fXRepository: FXRepository, private val selectedPairs: List<String>?) : PagingSource<Int, PopularPairTable>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularPairTable> = try {
        val loadPage = params.key ?: 0

        val results = fXRepository.getPopularCurrencyPairs(
            `apiKey` = API_KEY
        )
        val response = ArrayList<PopularPairTable>()
        results?.currencies?.forEach {
            val currencyPair = it.key
            val fullName = it.value
            val popularPairTable = PopularPairTable(`pair` = currencyPair, `fullName` = fullName)
            selectedPairs?.let { popularPairTable.isSelected = it?.contains(currencyPair)}
            response.add(popularPairTable)
        }

        LoadResult.Page(
            data = response,
            prevKey = null,
            nextKey = loadPage + 1
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }


    /*
    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, PopularPairTable>): Int? {
        return state.anchorPosition
    }
*/
}
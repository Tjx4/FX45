package com.platform45.fx45.ui.dashboard.pagging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.platform45.fx45.persistance.room.tables.popularPair.PopularPairTable
import com.platform45.fx45.repositories.FXRepository

class PopularPairPagingSource(private val fXRepository: FXRepository) : PagingSource<Int, PopularPairTable>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularPairTable> = try {
        val loadPage = params.key ?: 0
        /*

        val results = fXRepository.getPopularCurrencyPairs(
            `apiKey` = API_KEY

            val response = ArrayList<PopularPairTable>()
            results?.forEach { popularPairs.add(PopularPairTable(`pair` = it.key, `fullName` = it.value)) }
            response
        )
        */

        val response2 = ArrayList<PopularPairTable>()
        response2.add(PopularPairTable(`pair` = "Me1", `fullName` = "oh yes its me"))
        response2.add(PopularPairTable(`pair` = "Me2", `fullName` = "oh yes its me 2"))

        LoadResult.Page(
            data = response2,
            prevKey = null,
            nextKey = loadPage + 1
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, PopularPairTable>): Int? {
        return state.anchorPosition
    }

}
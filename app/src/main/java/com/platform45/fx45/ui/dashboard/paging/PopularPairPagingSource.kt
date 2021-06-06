package com.platform45.fx45.ui.dashboard.paging

import androidx.paging.PagingSource
import com.platform45.fx45.constants.API_KEY
import com.platform45.fx45.constants.PP_PAGE_SIZE
import com.platform45.fx45.persistance.room.tables.popularPair.PopularPairTable
import com.platform45.fx45.repositories.FXRepository
import java.lang.NullPointerException

class PopularPairPagingSource(private val fXRepository: FXRepository) : PagingSource<Int, PopularPairTable>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularPairTable> = try {
        val loadPage = params.key ?: 0

        val result = fXRepository.getPopularCurrencyPairs(
            `apiKey` = API_KEY
        )

        if (result == null || result?.error != null) {
            val error = result?.error
            LoadResult.Error(NullPointerException(error?.info ?: "Unknown error"))
        }
        else {
            val response = ArrayList<PopularPairTable>()
            result?.currencies?.forEach {
                val popularPairTable = PopularPairTable(`pair` = it.key, `fullName` = it.value)
                response.add(popularPairTable)
            }

            val pages = response.size / PP_PAGE_SIZE
            val currentPage = getCurrentPage(response, loadPage, PP_PAGE_SIZE)

            LoadResult.Page(
                data = currentPage,
                prevKey = null,
                nextKey = if (loadPage < pages) loadPage + 1 else null
            )
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    private fun getCurrentPage(response: List<PopularPairTable>, pageIndex: Int, pageSize: Int): List<PopularPairTable>{
        val pageData = response.withIndex().groupBy { it.index / pageSize }.values.map { pp -> pp.map { it.value } }
        return pageData[pageIndex]
    }

}
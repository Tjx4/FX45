package com.platform45.fx45.ui.dashboard.pagging

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
        if (result?.error != null) {
            val error = result?.error
            LoadResult.Error(NullPointerException(error?.info))
        }
        else {
            val response = ArrayList<PopularPairTable>()
            result?.currencies?.forEach {
                val popularPairTable = PopularPairTable(`pair` = it.key, `fullName` = it.value)
                response.add(popularPairTable)
            }

            val pages = response.size / PP_PAGE_SIZE

            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if (loadPage < pages) loadPage + 1 else null
            )
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

}
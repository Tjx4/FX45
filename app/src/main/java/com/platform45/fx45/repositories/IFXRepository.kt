package com.platform45.fx45.repositories

import com.platform45.fx45.models.Conversion
import com.platform45.fx45.models.Currencies
import com.platform45.fx45.models.Series

interface IFXRepository {
    suspend fun getConversion(api_key: String, from: String, to: String, amount: String): Conversion?

    suspend fun getPopularCurrencyPairs(apiKey: String) : Currencies?

    suspend fun getSeries(apiKey: String, startDate: String, endDate: String, currency: String, format: String) : Series?
}
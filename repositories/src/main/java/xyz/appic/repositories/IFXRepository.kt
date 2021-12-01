package xyz.appic.repositories

import xyz.appic.common.models.Conversion
import xyz.appic.common.models.Currencies
import xyz.appic.common.models.Series

interface IFXRepository {
    suspend fun getConversion(api_key: String, from: String, to: String, amount: String): Conversion?

    suspend fun getPopularCurrencyPairs(apiKey: String) : Currencies?

    suspend fun getSeries(apiKey: String, startDate: String, endDate: String, currency: String, format: String) : Series?
}
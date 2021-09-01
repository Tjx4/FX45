package com.platform45.fx45.repositories

import com.platform45.fx45.models.Currencies
import com.platform45.fx45.models.Series
import com.platform45.fx45.networking.retrofit.RetrofitHelper
import com.platform45.fx45.persistance.room.FX45Db
import com.platform45.fx45.models.Conversion

class FXRepository(private val retrofitHelper: RetrofitHelper, private val database: FX45Db) : IFXRepository{

    override suspend fun getConversion(api_key: String, from: String, to: String, amount: String): Conversion?{
        return try {
            retrofitHelper.convert(api_key, from, to, amount)
        }
        catch (ex: Exception){
            null
        }
    }

    override suspend fun getPopularCurrencyPairs(apiKey: String) : Currencies? {
        return try {
            retrofitHelper.currencies(apiKey)
        }
        catch (ex: Exception) {
            null
        }
    }

    override suspend fun getSeries(apiKey: String, startDate: String, endDate: String, currency: String, format: String) : Series? {
        return try {
            retrofitHelper.series(apiKey, startDate, endDate, currency, format)
        }
        catch (ex: Exception){
            null
        }
    }
}
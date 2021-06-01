package com.platform45.fx45.repositories

import com.platform45.fx45.models.Currencies
import com.platform45.fx45.networking.retrofit.RetrofitHelper
import com.platform45.fx45.persistance.room.FX45Db
import com.platform45.weather45.models.Conversion

class FXRepository(private val retrofitHelper: RetrofitHelper, private val database: FX45Db) {

    suspend fun getConversion(api_key: String, from: String, to: String, amount: String): Conversion?{
        return try {
            retrofitHelper.convert(api_key, from, to, amount)
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun getPopularCurrencyPairs(apiKey: String) : Currencies? {
        return try {
            retrofitHelper.currencies(apiKey)
        }
        catch (ex: Exception) {
            null
        }
    }


}
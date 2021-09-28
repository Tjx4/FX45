package com.platform45.fx45.repositories

import com.platform45.fx45.models.*
import com.platform45.fx45.networking.retrofit.Services
import com.platform45.fx45.persistance.room.FX45Db
import retrofit2.Response

class FXRepository(private val services: Services, private val database: FX45Db) : IFXRepository{

    override suspend fun getConversion(api_key: String, from: String, to: String, amount: String): Response<Conversion?> {
        return services.convert(api_key, from, to, amount)
    }

    override suspend fun getPopularCurrencyPairs(apiKey: String) : Currencies? {
        return try {
            services.currencies(apiKey)
        }
        catch (ex: Exception) {
            null
        }
    }

    override suspend fun getSeries(apiKey: String, startDate: String, endDate: String, currency: String, format: String) : Series? {
        return try {
            services.series(apiKey, startDate, endDate, currency, format)
        }
        catch (ex: Exception){
            null
        }
    }
}
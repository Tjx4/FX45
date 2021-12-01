package xyz.appic.core.networking.retrofit


import retrofit2.http.*
import xyz.appic.common.models.Conversion
import xyz.appic.common.models.Currencies
import xyz.appic.common.models.Series

interface RetrofitHelper {
    @GET("apiconvert")
    suspend fun convert(@Query("api_key") apiKey: String, @Query("from") from: String, @Query("to") to: String, @Query("amount")  amount: String): Conversion?

    @GET("apitimeseries")
    suspend fun series(@Query("api_key") apiKey: String, @Query("start_date") startDate: String, @Query("end_date") endDate: String, @Query("currency") currency: String, @Query("format") format: String): Series?

    @GET("apicurrencies")
    suspend fun currencies(@Query("api_key") apiKey: String): Currencies?
}
package com.platform45.fx45.networking.retrofit

import com.platform45.fx45.models.Currencies
import com.platform45.fx45.models.Series
import com.platform45.fx45.models.Conversion
import retrofit2.Response
import retrofit2.http.*

interface RetrofitHelper {
    @GET("apiconvert")
    suspend fun convert(@Query("api_key") apiKey: String, @Query("from") from: String, @Query("to") to: String, @Query("amount")  amount: String): Result<Conversion?>

    @GET("apitimeseries")
    suspend fun series(@Query("api_key") apiKey: String, @Query("start_date") startDate: String, @Query("end_date") endDate: String, @Query("currency") currency: String, @Query("format") format: String): Result<Series?>

    @GET("apicurrencies")
    suspend fun currencies(@Query("api_key") apiKey: String): Result<Currencies?>
}
package xyz.appic.repositories

import com.google.firebase.crashlytics.FirebaseCrashlytics
import xyz.appic.common.models.Conversion
import xyz.appic.common.models.Currencies
import xyz.appic.common.models.Series
import xyz.appic.core.networking.retrofit.RetrofitHelper
import xyz.appic.core.persistance.room.FX45Db


class FXRepository(private val retrofitHelper: RetrofitHelper, private val database: FX45Db, private val firebaseCrashlytics: FirebaseCrashlytics) :
    IFXRepository {

    override suspend fun getConversion(api_key: String, from: String, to: String, amount: String): Conversion?{
        return try {
            retrofitHelper.convert(api_key, from, to, amount)
        }
        catch (ex: Exception){
            firebaseCrashlytics.recordException(ex)
            null
        }
    }

    override suspend fun getPopularCurrencyPairs(apiKey: String) : Currencies? {
        return try {
            retrofitHelper.currencies(apiKey)
        }
        catch (ex: Exception) {
            firebaseCrashlytics.recordException(ex)
            null
        }
    }

    override suspend fun getSeries(apiKey: String, startDate: String, endDate: String, currency: String, format: String) : Series? {
        return try {
            retrofitHelper.series(apiKey, startDate, endDate, currency, format)
        }
        catch (ex: Exception){
            firebaseCrashlytics.recordException(ex)
            null
        }
    }
}
package com.platform45.fx45.repositories

import com.platform45.fx45.models.Currencies
import com.platform45.fx45.networking.retrofit.RetrofitHelper

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


    suspend fun getSeriesCache(apiKey: String, startDate: String, endDate: String, currency: String, format: String) : List<PairHistoryTable>{
        return try {
            val historyTables = ArrayList<PairHistoryTable>()
            val currencyPairs = currency.split(",")
            val price = retrofitHelper.series(apiKey, startDate, endDate, currency, format)?.price
            val pairHistories = getPairHistoryList(startDate, endDate, currencyPairs, price)
            pairHistories?.forEach {
                it?.let { pairHistory -> historyTables.add(pairHistory?.toDbTable()) }
            }

            historyTables
        }
        catch (ex: Exception){
            ArrayList<PairHistoryTable>()
        }
    }

    suspend fun getAllPairHistoriesFromDb() : List<PairTradeHistory?>??{
        return try {
            val tradeHistories = ArrayList<PairTradeHistory>()
            database.pairHistoryDAO.getAllHistories()?.forEach { tradeHistories.add(it.toPairHistory()) }
            tradeHistories
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun getPairTradeHistoryFromDb(id: Long) : PairTradeHistory?{
        return try {
            database.pairHistoryDAO.get(id)?.toPairHistory()
        }
        catch (ex: Exception){
            null
        }
    }

    suspend fun addPairTradeHistoryToDb(pairTradeHistory: PairTradeHistory) : DbOperation {
        return try {
            database.pairHistoryDAO.insert(pairTradeHistory.toDbTable())
            DbOperation(true)
        }
        catch (ex: Exception){
            DbOperation(false, "$ex")
        }
    }

    suspend fun addAllPairTradeHistoriesToDb(pairTradeHistories: List<PairTradeHistory?>?) : DbOperation {
        return try {
            val tradeTables = ArrayList<PairHistoryTable>()
            pairTradeHistories?.forEach {
               // tradeTables.add(it.toDbTable())
                it?.let { it1 -> addPairTradeHistoryToDb(it1) }
            }
            //database.pairHistoryDAO.insertAll(tradeTables)
            DbOperation(true)
        }
        catch (ex: Exception){
            DbOperation(false, "$ex")
        }
    }

}
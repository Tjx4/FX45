package xyz.appic.core.helpers

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import xyz.appic.common.models.DayData
import xyz.appic.common.models.PairTradeHistory

fun Object.toPricseLinkedTreeMap() = this as LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>?

fun String.toLinkedTreeMap(): LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>? {
    return Gson().fromJson(this,  object : TypeToken<LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>?>() {}.type)
}

fun String.toDayDataList(): List<DayData?>? {
    return Gson().fromJson(this,  object : TypeToken<List<DayData?>?>() {}.type)
}

fun LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>?.mapToString(): String {
    return if(this == null) "" else Gson().toJson(this)
}

fun List<DayData?>?.mapToString(): String {
    return if(this == null) "" else Gson().toJson(this)
}

fun getPairHistoryList(startDate: String, endDate: String, currencies: List<String>, prices: LinkedTreeMap<String?, LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>?>?>?): List<PairTradeHistory?>?{
    val tempPairTrades = ArrayList<PairTradeHistory>()

    prices?.let {
        for (currencyPair in currencies) {
            val currencyDateData = ArrayList<DayData>()
            for (currentPrice in it) {
                val currentDay =
                    currentPrice.value as LinkedTreeMap<String?, LinkedTreeMap<String?, Double?>>

                val dateData = currentDay[currencyPair]
                val seriesDateData = DayData(
                    currentPrice.key,
                    dateData?.get("close")?.toFloat(),
                    dateData?.get("high")?.toFloat(),
                    dateData?.get("low")?.toFloat(),
                    dateData?.get("open")?.toFloat()
                )
                currencyDateData.add(seriesDateData)
            }

            tempPairTrades.add(
                PairTradeHistory(
                    currencyPair,
                    startDate,
                    endDate,
                    currencyDateData
                )
            )
        }
    }

    return tempPairTrades
}

fun PairTradeHistory.toDbTable() = PairHistoryTable(`tradingPair` = this.tradingPair , `startDate` = this.startDate, `endDate` = this.endDate, `history` = this.history.mapToString())
fun PairHistoryTable.toPairHistory() =  PairTradeHistory(this.tradingPair, this.startDate, this.endDate, this.history?.toDayDataList())





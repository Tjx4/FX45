package xyz.appic.common.models

data class PairTradeHistory (
    var tradingPair: String?,
    var startDate: String?,
    var endDate: String?,
    var history: List<DayData?>?
)

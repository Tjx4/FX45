package xyz.appic.common.helpers

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDate() : String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(Date())
}

fun getDaysAgo(daysAgo: Int) : String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(calendar.time)
}

fun getClosestWeekDay(daysAgo: Int): String {
    //Todo fix
    val calendar = Calendar.getInstance()
    var dayOfWeek = Calendar.DAY_OF_WEEK
    var requiredDays = daysAgo

    when (dayOfWeek) {
        1 -> requiredDays = daysAgo + 1
        7 -> requiredDays = daysAgo - 2
    }

    calendar.add(Calendar.DAY_OF_YEAR, -requiredDays)

    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(calendar.time)
}
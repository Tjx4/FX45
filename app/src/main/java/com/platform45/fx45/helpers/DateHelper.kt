package com.platform45.fx45.helpers

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDate():String{
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(Date())
}

fun getDaysAgo(daysAgo: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(calendar.time)
}

fun getClosestWeekDay(daysAgo: Int): String{
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    var dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)

    when {
        dayOfWeek == 1 -> calendar.add(Calendar.DAY_OF_YEAR, +1)
        dayOfWeek == 7 -> calendar.add(Calendar.DAY_OF_YEAR, -1)
        else -> calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    }

    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(calendar.time)
}
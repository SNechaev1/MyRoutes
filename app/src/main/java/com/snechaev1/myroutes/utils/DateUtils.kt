package com.snechaev1.myroutes.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    val routeDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    val date = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    val time = SimpleDateFormat("HH:mm", Locale.US)

//    fun parseServerDate(string: String) = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(string).toString()

    fun sameDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) {
            return false
        }
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        val sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
        return sameDay
    }
}
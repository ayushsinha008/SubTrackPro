package com.subtrackpro.app.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun format(ts: Long, pattern: String = "dd MMM yyyy"): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(Date(ts))

    fun calcNextBillingDate(start: Long, cycle: String): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = start }
        val now = Calendar.getInstance()
        while (cal.before(now)) {
            if (cycle == "MONTHLY") cal.add(Calendar.MONTH, 1) else cal.add(Calendar.YEAR, 1)
        }
        return cal.timeInMillis
    }

    fun daysBetween(from: Long, to: Long): Int =
        ((to - from) / (1000 * 60 * 60 * 24)).toInt()
}

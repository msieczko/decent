package eu.bwbw.decent.utils

import java.text.SimpleDateFormat
import java.util.*

object DateConverters {
    @JvmStatic
    fun secondsToDateTimeString(seconds: Int): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.GERMAN).format(
            Date(seconds.toLong() * 1000)
        )
    }

    @JvmStatic
    fun secondsToHours(seconds: Int): String {
        return "${seconds / 3600} h"
    }
}
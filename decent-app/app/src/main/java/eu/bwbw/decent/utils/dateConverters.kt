package eu.bwbw.decent.utils

import java.text.SimpleDateFormat
import java.util.*

fun secondsToDateTimeString(seconds: Int): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.GERMAN).format(
        Date(seconds.toLong() * 1000)
    )
}
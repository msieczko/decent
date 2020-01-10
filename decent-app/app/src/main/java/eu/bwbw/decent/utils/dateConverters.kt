package eu.bwbw.decent.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun secondsToDateTimeString(seconds: Int): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm").format(
        Date(seconds.toLong() * 1000)
    )
}
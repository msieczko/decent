package eu.bwbw.decent.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DateConvertersKtTest {
    @Test
    fun `converts date from seconds to date time string`() {
        val dateTimeString = secondsToDateTimeString(1578691078)
        assertEquals("2020-01-10 22:17", dateTimeString)
    }
}
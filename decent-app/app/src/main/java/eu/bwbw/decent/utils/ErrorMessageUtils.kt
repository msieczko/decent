package eu.bwbw.decent.utils

object ErrorMessageUtils {
    fun formMessage(message: String, reason: String?, cause: Throwable?): String {
        if (reason != null) {
            return "$message, $reason"
        }
        if (cause?.message != null) {
            return "$message, ${cause.message}"
        }
        return message
    }
}
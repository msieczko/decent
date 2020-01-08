package eu.bwbw.decent.domain.errors.transactions

open class CourierServiceExecutionError(
    message: String,
    cause: Throwable?
) : Throwable("CourierService contract execution error: $message", cause)
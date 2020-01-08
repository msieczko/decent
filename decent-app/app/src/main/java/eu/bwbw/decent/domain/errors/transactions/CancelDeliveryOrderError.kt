package eu.bwbw.decent.domain.errors.transactions

import eu.bwbw.decent.utils.formErrorMessage

class CancelDeliveryOrderError(
    reason: String? = null,
    cause: Throwable? = null
) : CourierServiceExecutionError(
    formErrorMessage("failed to cancel delivery order", reason, cause),
    cause
)
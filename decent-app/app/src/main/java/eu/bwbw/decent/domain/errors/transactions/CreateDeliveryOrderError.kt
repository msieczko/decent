package eu.bwbw.decent.domain.errors.transactions

import eu.bwbw.decent.utils.formErrorMessage

class CreateDeliveryOrderError(
    reason: String? = null,
    cause: Throwable? = null
) : CourierServiceExecutionError(
    formErrorMessage("failed to create delivery order", reason, cause),
    cause
)
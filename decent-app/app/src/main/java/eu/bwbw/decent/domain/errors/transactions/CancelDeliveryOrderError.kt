package eu.bwbw.decent.domain.errors.transactions

import eu.bwbw.decent.utils.ErrorMessageUtils.formMessage

class CancelDeliveryOrderError(
    reason: String? = null,
    cause: Throwable? = null
) : CourierServiceExecutionError(
    formMessage("failed to cancel delivery order", reason, cause),
    cause
)
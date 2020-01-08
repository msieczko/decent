package eu.bwbw.decent.domain.errors.transactions

import eu.bwbw.decent.utils.ErrorMessageUtils.formMessage

class CreateDeliveryOrderError(
    reason: String? = null,
    cause: Throwable? = null
) : CourierServiceExecutionError(
    formMessage("failed to create delivery order", reason, cause),
    cause
)
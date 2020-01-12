package eu.bwbw.decent.domain.errors.transactions

import eu.bwbw.decent.utils.formErrorMessage

class PickupPackageError(
    reason: String? = null,
    cause: Throwable? = null
) : CourierServiceExecutionError(
    formErrorMessage("failed to pickup package", reason, cause),
    cause
)
package eu.bwbw.decent.domain.errors.transactions

import eu.bwbw.decent.utils.formErrorMessage

class DeliverPackageError(
    reason: String? = null,
    cause: Throwable? = null
) : CourierServiceExecutionError(
    formErrorMessage("failed to deliver package", reason, cause),
    cause
)
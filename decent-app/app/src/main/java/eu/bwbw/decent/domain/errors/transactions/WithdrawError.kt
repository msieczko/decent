package eu.bwbw.decent.domain.errors.transactions

import eu.bwbw.decent.utils.formErrorMessage

class WithdrawError(
    reason: String? = null,
    cause: Throwable? = null
) : CourierServiceExecutionError(
    formErrorMessage("failed to withdraw funds", reason, cause),
    cause
)
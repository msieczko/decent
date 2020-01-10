package eu.bwbw.decent.domain

enum class DeliveryState {
    OFFER,
    PICKUP_DECLARED,
    IN_DELIVERY,
    DELIVERED,
    OFFER_CANCELED,
    RETURNED,
    COURIER_REFUND_CLAIM,
    COURIER_REFUNDED,
    SENDER_REQUESTED_RETURN,
    SENDER_PARTIALLY_REFUNDED,
    DELIVERY_TIME_PASSED,
    SENDER_REFUNDED;

    companion object {
        fun fromInt(value: Int) = values().first { it.ordinal == value }
    }
}
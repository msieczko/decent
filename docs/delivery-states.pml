@startuml delivery-states
hide empty description

title Delivery States

state HappyPath {
    [*] -> OFFER
    OFFER --> PICKUP_DECLARED
    PICKUP_DECLARED --> OFFER
    PICKUP_DECLARED --> IN_DELIVERY
    OFFER --> IN_DELIVERY
    OFFER --> OFFER_CANCELED
    IN_DELIVERY --> DELIVERED
    DELIVERED --> [*]
    OFFER_CANCELED --> [*]
    IN_DELIVERY --> RETURNED
    RETURNED --> [*]
}

state Dispute {
    IN_DELIVERY -up-> COURIER_REFUND_CLAIM
    IN_DELIVERY --> DELIVERY_TIME_PASSED
    DELIVERY_TIME_PASSED --> RETURNED
    DELIVERY_TIME_PASSED --> SENDER_REFUNDED
    SENDER_REFUNDED --> [*]
    COURIER_REFUND_CLAIM --> COURIER_REFUNDED
    COURIER_REFUNDED --> [*]
    COURIER_REFUND_CLAIM --> SENDER_REQUESTED_RETURN
    SENDER_REQUESTED_RETURN --> RETURNED
    SENDER_REQUESTED_RETURN --> SENDER_PARTIALLY_REFUNDED
    SENDER_PARTIALLY_REFUNDED --> [*]
}

@enduml

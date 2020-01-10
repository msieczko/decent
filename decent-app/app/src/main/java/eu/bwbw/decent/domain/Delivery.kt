package eu.bwbw.decent.domain

import java.math.BigInteger

data class Delivery(
    var id: BigInteger,
    var title: String,
    var description: String,
    var receiverPostalAddress: String,
    var courierDeposit: BigInteger,
    var courierAward: BigInteger,
    var maxDeliveryTime: Int
)





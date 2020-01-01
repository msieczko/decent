package eu.bwbw.decent.domain

import java.util.*

data class Delivery(
    var title: String,
    var description: String,
    var receiverEthereumAddress: String,
    var receiverPostalAddress: String,
    var courierDeposit: Int,
    var courierAward: Int,
    var maxDeliveryTime: Long
)





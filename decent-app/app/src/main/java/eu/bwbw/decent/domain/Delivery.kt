package eu.bwbw.decent.domain

data class Delivery(
    var id: Int,
    var title: String,
    var description: String,
    var receiverEthereumAddress: String,
    var receiverPostalAddress: String,
    var courierDeposit: Int,
    var courierAward: Int,
    var maxDeliveryTime: Long
)





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
) {
    companion object {
        val LIST_MOCK: MutableList<Delivery> = Arrays.asList(
            Delivery(
                "Zlecenie przewozu kota",
                "Duży, rudy w koszu",
                "r",
                "ul. Kwiatowa 14/12, Warszawa",
                200,
                20,
                10
            ),
            Delivery(
                "Zlecenie przewozu dużego dzika",
                "Duży, dziki w koszu",
                "r",
                "ul. Andaluzyjska 11/1, Warszawa",
                500,
                80,
                30
            )
        )
    }
}





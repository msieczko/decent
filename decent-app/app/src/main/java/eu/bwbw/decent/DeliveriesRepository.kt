package eu.bwbw.decent

import eu.bwbw.decent.domain.Delivery
import java.util.*
import kotlin.collections.ArrayList

class DeliveriesRepository(
) {
    private val deliveries = ArrayList<Delivery>(LIST_MOCK)

    fun getDeliveries(): List<Delivery> {
        return deliveries
    }

    fun saveDelivery(delivery: Delivery, onDeliverySaved: () -> Unit) {
        println("Saving delivery $delivery") // TODO REMOVE
        // simulate saving operation that takes 2 seconds
        Thread(Runnable {
            try {
                Thread.sleep(2000)
                deliveries.add(delivery)
                onDeliverySaved()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }).start()
    }

    companion object {
        private val LIST_MOCK: MutableList<Delivery> = Arrays.asList(
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
package eu.bwbw.decent

import eu.bwbw.decent.domain.Delivery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class DeliveriesRepository {
    private val deliveries = ArrayList<Delivery>(LIST_MOCK)
    private var count: Int = LIST_MOCK.size

    fun getDeliveries(): List<Delivery> {
        return deliveries
    }

    suspend fun saveDelivery(delivery: Delivery) {
        println("Saving delivery $delivery") // TODO REMOVE
        // simulate saving operation that takes 2 seconds
        withContext(Dispatchers.IO) {
            delay(2_000)
            delivery.id = ++count
            deliveries.add(delivery)
        }
    }

    fun getDelivery(deliveryId: Int, deliveryFetchedCallback: (Delivery) -> Unit) {
        deliveryFetchedCallback(deliveries.first { delivery -> delivery.id == deliveryId })
    }

    fun remove(deliveryId: Int) {
        deliveries.removeAll { delivery -> delivery.id == deliveryId }
        count--
    }

    companion object {
        private val LIST_MOCK: MutableList<Delivery> = Arrays.asList(
            Delivery(
                1,
                "Zlecenie przewozu kota",
                "Duży, rudy w koszu",
                "r",
                "ul. Kwiatowa 14/12, Warszawa",
                200,
                20,
                10
            ),
            Delivery(
                2,
                "Zlecenie przewozu dużego dzika",
                "Duży, dziki w koszu",
                "r",
                "ul. Andaluzyjska 11/1, Warszawa",
                500,
                80,
                30
            )
        )

        private val SINGLE_MOCK = Delivery(
            1,
            "Zlecenie przewozu kota",
            "Duży, rudy w koszu",
            "r",
            "ul. Kwiatowa 14/12, Warszawa",
            200,
            20,
            10
        )
    }
}
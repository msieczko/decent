package eu.bwbw.decent.services

import eu.bwbw.decent.domain.ContractDelivery
import eu.bwbw.decent.domain.Delivery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList

class DeliveriesService(
    private val deliveryDetailsRepository: IDeliveryDetailsRepository,
    private val courierServiceContractAddress: String,
    private val web3j: Web3j
) {

    private val deliveries = ArrayList<Delivery>(LIST_MOCK)
    private var count: Int = LIST_MOCK.size

    suspend fun getSenderDeliveries(credentials: Credentials): List<Delivery> {
        val courierServiceRepository = CourierServiceRepository(courierServiceContractAddress, web3j, credentials)
        return courierServiceRepository.getSenderDeliveries().map { contractDelivery: ContractDelivery ->
            deliveryFromContract(contractDelivery)
        }
    }

    suspend fun getCourierDeliveries(credentials: Credentials): List<Delivery> {
        val courierServiceRepository = CourierServiceRepository(courierServiceContractAddress, web3j, credentials)
        return courierServiceRepository.getCourierDeliveries().map { contractDelivery: ContractDelivery ->
            deliveryFromContract(contractDelivery)
        }
    }

    suspend fun getReceiverDeliveries(credentials: Credentials): List<Delivery> {
        // TODO
        return listOf()
    }

    private suspend fun deliveryFromContract(
        contractDelivery: ContractDelivery
    ): Delivery {
        val deliveryDetails = deliveryDetailsRepository.get(contractDelivery.detailsHash)
        return Delivery(
            contractDelivery.id,
            deliveryDetails?.title ?: "no title",
            deliveryDetails?.description ?: "no description",
            deliveryDetails?.receiverPostalAddress ?: "no address",
            contractDelivery.courierDeposit,
            contractDelivery.courierAward,
            contractDelivery.pickupDeadline
        )
    }

    suspend fun saveDelivery(delivery: Delivery) {
        println("Saving delivery $delivery") // TODO REMOVE
        // simulate saving operation that takes 2 seconds
        withContext(Dispatchers.IO) {
            delay(2_000)
            delivery.id = (++count).toBigInteger()
            deliveries.add(delivery)
        }
    }

    fun getDelivery(deliveryId: BigInteger, deliveryFetchedCallback: (Delivery) -> Unit) {
        deliveryFetchedCallback(deliveries.first { delivery -> delivery.id == deliveryId })
    }

    fun remove(deliveryId: BigInteger) {
        deliveries.removeAll { delivery -> delivery.id == deliveryId }
        count--
    }

    companion object {
        private val LIST_MOCK: MutableList<Delivery> = Arrays.asList(
            Delivery(
                1.toBigInteger(),
                "Zlecenie przewozu kota",
                "Duży, rudy w koszu",
                "ul. Kwiatowa 14/12, Warszawa",
                200.toBigInteger(),
                20.toBigInteger(),
                10
            ),
            Delivery(
                2.toBigInteger(),
                "Zlecenie przewozu dużego dzika",
                "Duży, dziki w koszu",
                "ul. Andaluzyjska 11/1, Warszawa",
                500.toBigInteger(),
                80.toBigInteger(),
                30
            )
        )

        private val SINGLE_MOCK = Delivery(
            1.toBigInteger(),
            "Zlecenie przewozu kota",
            "Duży, rudy w koszu",
            "ul. Kwiatowa 14/12, Warszawa",
            200.toBigInteger(),
            20.toBigInteger(),
            10
        )
    }
}
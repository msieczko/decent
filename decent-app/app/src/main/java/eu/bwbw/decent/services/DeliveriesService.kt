package eu.bwbw.decent.services

import eu.bwbw.decent.domain.ContractDelivery
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.utils.weiToString
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

    //private val deliveries = ArrayList<Delivery>(LIST_MOCK)

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
            weiToString(contractDelivery.courierDeposit.toBigDecimal()),
            weiToString(contractDelivery.courierAward.toBigDecimal()),
            contractDelivery.pickupDeadline
        )
    }

    fun getDelivery(deliveryId: BigInteger, deliveryFetchedCallback: (Delivery) -> Unit) {
        TODO("implement")
    }

    fun remove(deliveryId: BigInteger) {
       TODO("implement")
    }
}
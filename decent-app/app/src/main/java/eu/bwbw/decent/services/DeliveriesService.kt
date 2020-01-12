package eu.bwbw.decent.services

import eu.bwbw.decent.domain.ContractDelivery
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.ui.sender.addnewdelivery.SanitizedDelivery
import eu.bwbw.decent.utils.weiToString
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import java.math.BigInteger

class DeliveriesService(
    private val deliveryDetailsRepository: IDeliveryDetailsRepository,
    private val courierServiceContractAddress: String,
    private val web3j: Web3j
) {

    private lateinit var deliveries: List<Delivery>
    // TODO change to proper method call

    suspend fun createDeliveryOrder(sanitizedDelivery: SanitizedDelivery, credentials: Credentials): BigInteger {
        val (title, description, receiverEthAddress, receiverPostalAddress, courierDeposit, courierAward, maxDeliveryTime) = sanitizedDelivery
        val detailsHash = deliveryDetailsRepository.save(
            DeliveryDetails(
                title,
                description,
                receiverPostalAddress
            )
        )

        val courierServiceRepository = CourierServiceRepository(courierServiceContractAddress, web3j, credentials)
        val deliveryId = courierServiceRepository.createDeliveryOrder(
            DeliveryOrder(
                receiverEthAddress,
                courierDeposit,
                courierAward,
                maxDeliveryTime,
                detailsHash
            )
        )
        return deliveryId
    }

    suspend fun getSenderDeliveries(credentials: Credentials): List<Delivery> {
        val courierServiceRepository = CourierServiceRepository(courierServiceContractAddress, web3j, credentials)
        deliveries = courierServiceRepository.getSenderDeliveries().map { contractDelivery: ContractDelivery ->
            deliveryFromContract(contractDelivery)
        }
        return deliveries
    }

    suspend fun getCourierDeliveries(credentials: Credentials): List<Delivery> {
        val courierServiceRepository = CourierServiceRepository(courierServiceContractAddress, web3j, credentials)
        deliveries = courierServiceRepository.getSenderDeliveries().map { contractDelivery: ContractDelivery ->
            deliveryFromContract(contractDelivery)
        }
        return deliveries
    }

    suspend fun getReceiverDeliveries(credentials: Credentials): List<Delivery> {
        val courierServiceRepository = CourierServiceRepository(courierServiceContractAddress, web3j, credentials)
        deliveries = courierServiceRepository.getSenderDeliveries().map { contractDelivery: ContractDelivery ->
            deliveryFromContract(contractDelivery)
        }
        return deliveries
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
            contractDelivery.deliveryDeadline,
            contractDelivery.state
        )
    }

    fun getDelivery(deliveryId: BigInteger): Delivery {
        return deliveries.first { it.id == deliveryId }
    }
}
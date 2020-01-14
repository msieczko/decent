package eu.bwbw.decent.services

import eu.bwbw.decent.domain.ContractDelivery
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.services.deliverydetails.DeliveryDetails
import eu.bwbw.decent.services.deliverydetails.IDeliveryDetailsRepository
import eu.bwbw.decent.ui.sender.addnewdelivery.SanitizedDelivery
import eu.bwbw.decent.utils.weiToString
import java.math.BigInteger

class DeliveriesService(
    private val courierServiceRepository: CourierServiceRepository,
    private val deliveryDetailsRepository: IDeliveryDetailsRepository
) {

    private lateinit var senderDeliveries: List<Delivery>
    private lateinit var courierDeliveries: List<Delivery>
    private lateinit var receiverDeliveries: List<Delivery>
    // TODO change to proper method call

    suspend fun createDeliveryOrder(sanitizedDelivery: SanitizedDelivery): BigInteger {
        val (title, description, receiverEthAddress, receiverPostalAddress, courierDeposit, courierAward, maxDeliveryTime) = sanitizedDelivery
        val detailsHash = deliveryDetailsRepository.save(
            DeliveryDetails(
                title,
                description,
                receiverPostalAddress
            )
        )

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

    suspend fun cancelDeliveryOrder(deliveryId: BigInteger) = courierServiceRepository.cancelDeliveryOrder(deliveryId)

    suspend fun pickupPackage(deliveryId: BigInteger) = courierServiceRepository.pickupPackage(deliveryId)

    suspend fun getSenderDeliveries(): List<Delivery> {
        senderDeliveries = courierServiceRepository.getSenderDeliveries().map { deliveryFromContract(it) }
        return senderDeliveries
    }

    suspend fun getCourierDeliveries(): List<Delivery> {
        courierDeliveries = courierServiceRepository.getCourierDeliveries().map { deliveryFromContract(it) }
        return courierDeliveries
    }

    suspend fun getReceiverDeliveries(): List<Delivery> {
        receiverDeliveries = courierServiceRepository.getSenderDeliveries().map { deliveryFromContract(it) }
        return receiverDeliveries
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
        return senderDeliveries.firstOrNull { it.id == deliveryId }
            ?: courierDeliveries.firstOrNull { it.id == deliveryId }
            ?: receiverDeliveries.first { it.id == deliveryId }
    }
}
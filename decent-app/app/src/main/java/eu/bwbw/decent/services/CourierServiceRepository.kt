package eu.bwbw.decent.services

import eu.bwbw.decent.contracts.generated.CourierService
import eu.bwbw.decent.domain.ContractDelivery
import eu.bwbw.decent.domain.DeliveryState
import eu.bwbw.decent.domain.EthAddress
import eu.bwbw.decent.domain.errors.transactions.CancelDeliveryOrderError
import eu.bwbw.decent.domain.errors.transactions.CreateDeliveryOrderError
import eu.bwbw.decent.domain.errors.transactions.PickupPackageError
import eu.bwbw.decent.services.userdata.IUserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Numeric
import java.math.BigInteger

data class DeliveryOrder(
    val receiver: EthAddress,
    val courierDeposit: BigInteger,
    val courierAward: BigInteger,
    val maxDeliveryTime: Int, // seconds
    val detailsHash: String
)

class CourierServiceRepository(
    private val contractAddress: String,
    private val web3j: Web3j,
    private val userDataRepository: IUserDataRepository
) {
    private val courierService
        get() = CourierService.load(contractAddress, web3j, userDataRepository.getCredentials(), DefaultGasProvider())

    suspend fun createDeliveryOrder(delivery: DeliveryOrder): BigInteger {
        try {
            val (receiver, courierDeposit, courierAward, maxDeliveryTime, detailsHash) = delivery
            val senderDeposit = courierDeposit.div(BigInteger.valueOf(2)).add(courierAward)

            return withContext(Dispatchers.IO) {
                val createDeliveryOrder = courierService.createDeliveryOrder(
                    receiver.normalized,
                    courierDeposit,
                    courierAward,
                    BigInteger.valueOf(maxDeliveryTime.toLong()),
                    Numeric.hexStringToByteArray(detailsHash),
                    senderDeposit
                )
                val transactionReceipt = createDeliveryOrder.send()
                val (deliveryCreatedEvent) = courierService.getDeliveryCreatedEvents(transactionReceipt)
                deliveryCreatedEvent.deliveryId
            }
        } catch (e: Exception) {
            throw CreateDeliveryOrderError(cause = e)
        }
    }

    suspend fun cancelDeliveryOrder(deliveryId: BigInteger) {
        try {
            withContext(Dispatchers.IO) {
                val transactionReceipt = courierService.cancelDeliveryOrder(deliveryId).send()
                val deliveryCanceledEvents = courierService.getDeliveryCanceledEvents(transactionReceipt)
                if (deliveryCanceledEvents.size != 1) {
                    throw CancelDeliveryOrderError("DeliveryCanceled event not logged")
                }
            }
        } catch (e: Exception) {
            throw CancelDeliveryOrderError(cause = e)
        }
    }

    suspend fun pickupPackage(deliveryId: BigInteger) {
        try {
            withContext(Dispatchers.IO) {
                val delivery = courierService.deliveries(deliveryId).send()
                val contractDelivery = ContractDelivery.fromTuple(delivery)

                val transactionReceipt = courierService.pickupPackage(
                    deliveryId, contractDelivery.courierDeposit
                ).send()
                val packagePickedUpEvents = courierService.getPackagePickedUpEvents(transactionReceipt)
                if (packagePickedUpEvents.size != 1) {
                    throw PickupPackageError("PackagePickup event not logged")
                }
            }
        } catch (e: Exception) {
            throw PickupPackageError(cause = e)
        }
    }

    suspend fun getSenderDeliveries(): List<ContractDelivery> {
        return withContext(Dispatchers.IO) {
            val senderAddress = userDataRepository.getCredentials().address
            val count = courierService.getSenderDeliveriesCount(senderAddress).send().toInt()
            println("count Sender: $count")
            (0 until count).map { BigInteger.valueOf(it.toLong()) }
                .map { courierService.senderDeliveries(senderAddress, it).send() }
                .map { courierService.deliveries(it).send() }
                .map { ContractDelivery.fromTuple(it) }
                .filter { it.state != DeliveryState.OFFER_CANCELED }
        }
    }

    suspend fun getCourierDeliveries(): List<ContractDelivery> {
        return withContext(Dispatchers.IO) {
            val courierAddress = userDataRepository.getCredentials().address
            val count = courierService.getCourierDeliveriesCount(courierAddress).send().toInt()
            println("count Courier: $count")
            (0 until count).map { BigInteger.valueOf(it.toLong()) }
                .map { courierService.courierDeliveries(courierAddress, it).send() }
                .map { courierService.deliveries(it).send() }
                .map { ContractDelivery.fromTuple(it) }
        }
    }

    suspend fun getReceiverDeliveries(): List<ContractDelivery> {
        return withContext(Dispatchers.IO) {
            val receiverAddress = userDataRepository.getCredentials().address
            (courierService.getReceiverDeliveries(receiverAddress).send() as List<BigInteger>?)
                ?.map { courierService.courierDeliveries(receiverAddress, it).send() }
                ?.map { courierService.deliveries(it).send() }
                ?.map { ContractDelivery.fromTuple(it) }
                .orEmpty()
        }
    }

}
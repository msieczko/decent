package eu.bwbw.decent.services

import eu.bwbw.decent.contracts.generated.CourierService
import eu.bwbw.decent.domain.ContractDelivery
import eu.bwbw.decent.domain.DeliveryState
import eu.bwbw.decent.domain.EthAddress
import eu.bwbw.decent.domain.errors.transactions.CancelDeliveryOrderError
import eu.bwbw.decent.domain.errors.transactions.CreateDeliveryOrderError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
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
    contractAddress: String,
    web3j: Web3j,
    private val credentials: Credentials
) {
    private val courierService = CourierService.load(contractAddress, web3j, credentials, DefaultGasProvider())

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

    suspend fun getSenderDeliveries(): List<ContractDelivery> {
        return withContext(Dispatchers.IO) {
            val count = courierService.getSenderDeliveriesCount(credentials.address).send().toInt()
            println("count Sender: $count")
            (0 until count).map { BigInteger.valueOf(it.toLong()) }
                .map { courierService.senderDeliveries(credentials.address, it).send() }
                .map { courierService.deliveries(it).send() }
                .map { ContractDelivery.fromTuple(it) }
                .filter { it.state != DeliveryState.OFFER_CANCELED }
        }
    }

    suspend fun getCourierDeliveries(): List<ContractDelivery> {
        return withContext(Dispatchers.IO) {
            val count = courierService.getCourierDeliveriesCount(credentials.address).send().toInt()
            println("count Courier: $count")
            (0 until count).map { BigInteger.valueOf(it.toLong()) }
                .map { courierService.courierDeliveries(credentials.address, it).send() }
                .map { courierService.deliveries(it).send() }
                .map { ContractDelivery.fromTuple(it) }
        }
    }

    suspend fun getReceiverDeliveries(): List<ContractDelivery> {
        return withContext(Dispatchers.IO) {
            (courierService.getReceiverDeliveries(credentials.address).send() as List<BigInteger>?)
                ?.map { courierService.courierDeliveries(credentials.address, it).send() }
                ?.map { courierService.deliveries(it).send() }
                ?.map { ContractDelivery.fromTuple(it) }
                .orEmpty()
        }
    }

}
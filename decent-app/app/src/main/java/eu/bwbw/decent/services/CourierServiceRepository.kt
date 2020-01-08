package eu.bwbw.decent.services

import eu.bwbw.decent.contracts.generated.CourierService
import eu.bwbw.decent.domain.ContractDelivery
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.domain.errors.transactions.CancelDeliveryOrderError
import eu.bwbw.decent.domain.errors.transactions.CreateDeliveryOrderError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash.sha3
import org.web3j.protocol.Web3j
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert.Unit.ETHER
import org.web3j.utils.Convert.toWei
import java.math.BigInteger

class CourierServiceRepository(
    contractAddress: String,
    web3j: Web3j,
    private val credentials: Credentials
) {
    private val courierService = CourierService.load(contractAddress, web3j, credentials, DefaultGasProvider())

    suspend fun createDeliveryOrder(delivery: Delivery): BigInteger {
        val (_, title) = delivery

        val courierDeposit = toWei("1", ETHER).toBigIntegerExact()
        val courierAward = toWei("0.2", ETHER).toBigIntegerExact()
        val senderDeposit = courierDeposit.div(BigInteger.valueOf(2)).add(courierAward)

        try {
            return withContext(Dispatchers.IO) {
                val createDeliveryOrder = courierService.createDeliveryOrder(
                    "D1D84F0e28D6fedF03c73151f98dF95139700aa7",
                    courierDeposit,
                    courierAward,
                    BigInteger.valueOf(12 * 3600),
                    sha3(title.toByteArray()),
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
            (1..count).map { BigInteger.valueOf(it.toLong()) }
                .map { courierService.senderDeliveries(credentials.address, it).send() }
                .map { courierService.deliveries(it).send() }
                .map { ContractDelivery.fromTuple(it) }
        }
    }

    suspend fun getCourierDeliveries(): List<ContractDelivery> {
        return withContext(Dispatchers.IO) {
            val count = courierService.getCourierDeliveriesCount(credentials.address).send().toInt()
            (1..count).map { BigInteger.valueOf(it.toLong()) }
                .map { courierService.courierDeliveries(credentials.address, it).send() }
                .map { courierService.deliveries(it).send() }
                .map { ContractDelivery.fromTuple(it) }
        }
    }

}
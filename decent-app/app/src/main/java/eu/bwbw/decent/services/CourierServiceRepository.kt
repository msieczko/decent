package eu.bwbw.decent.services

import eu.bwbw.decent.domain.ContractDelivery
import eu.bwbw.decent.domain.DeliveryState
import eu.bwbw.decent.domain.EthAddress
import eu.bwbw.decent.domain.errors.DecentCourierService
import eu.bwbw.decent.domain.errors.transactions.*
import eu.bwbw.decent.services.userdata.IUserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
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
    private var previousCredentials: Credentials? = null
    private var _courierService: DecentCourierService? = null
    private fun getCourierService(): DecentCourierService {
        val currentCredentials = userDataRepository.getCredentials()
        if (previousCredentials != currentCredentials) {
            previousCredentials = currentCredentials
            _courierService = DecentCourierService(contractAddress, web3j, currentCredentials, DefaultGasProvider())
        }
        return _courierService ?: throw Error("This should never happen")
    }

    suspend fun createDeliveryOrder(delivery: DeliveryOrder): BigInteger {
        val courierService = getCourierService()
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
        val courierService = getCourierService()
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
        val courierService = getCourierService()
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
        val courierService = getCourierService()
        return withContext(Dispatchers.IO) {
            val senderAddress = userDataRepository.getCredentials().address
            val count = courierService.getSenderDeliveriesCount(senderAddress).send().toInt()
            println("count Sender: $count")
            (0 until count).asSequence()
                .map { BigInteger.valueOf(it.toLong()) }
                .map { courierService.senderDeliveries(senderAddress, it).send() }
                .map { courierService.deliveries(it).send() }
                .map { ContractDelivery.fromTuple(it) }
                .filter { it.state != DeliveryState.OFFER_CANCELED }
                .toList()
        }
    }

    suspend fun getCourierDeliveries(): List<ContractDelivery> {
        val courierService = getCourierService()
        return withContext(Dispatchers.IO) {
            val logs = courierService.getAllLogs()
            val allCreatedIds = courierService.extractDeliveryCreatedEvents(logs).map { it.deliveryId }
            val allCanceledIds = courierService.extractDeliveryCanceledEvents(logs).map { it.deliveryId }
            val allPickedUpIds = courierService.extractPackagePickedUpEvents(logs).map { it.deliveryId }
            val currentDeliveryOffers =
                allCreatedIds.filter { !allCanceledIds.contains(it) && !allPickedUpIds.contains(it) }
                    .map { courierService.deliveries(it).send() }
                    .map { ContractDelivery.fromTuple(it) }

            val courierAddress = userDataRepository.getCredentials().address
            val count = courierService.getCourierDeliveriesCount(courierAddress).send().toInt()
            val handledDeliveries = (0 until count).map { BigInteger.valueOf(it.toLong()) }
                .map { courierService.courierDeliveries(courierAddress, it).send() }
                .map { courierService.deliveries(it).send() }
                .map { ContractDelivery.fromTuple(it) }
                .filter { it.state != DeliveryState.DELIVERED }

            currentDeliveryOffers + handledDeliveries
        }
    }

    suspend fun getReceiverDeliveries(): List<ContractDelivery> {
        val courierService = getCourierService()
        return withContext(Dispatchers.IO) {
            val receiverAddress = userDataRepository.getCredentials().address
            val count = courierService.getReceiverDeliveriesCount(receiverAddress).send().toInt()
            println("count Receiver: $count")
            (0 until count).asSequence()
                .map { BigInteger.valueOf(it.toLong()) }
                .map { courierService.receiverDeliveries(receiverAddress, it).send() }
                .map { courierService.deliveries(it).send() }
                .map { ContractDelivery.fromTuple(it) }
                .filter { it.state == DeliveryState.IN_DELIVERY }
                .toList()
        }
    }

    suspend fun withdrawMoney() {
        val courierService = getCourierService()
        try {
            return withContext(Dispatchers.IO) {
                val transactionReceipt = courierService.withdraw().send()
                val fundsWithdrawnEvents = courierService.getFundsWithdrawnEvents(transactionReceipt)
                if (fundsWithdrawnEvents.size != 1) {
                    throw WithdrawError("Withdraw event not logged")
                }
            }
        } catch (e: Exception) {
            throw WithdrawError("Withdraw event not logged")
        }
    }

    suspend fun getWithdrawal(): BigInteger {
        val courierService = getCourierService()
        return withContext(Dispatchers.IO) {
            courierService.pendingWithdrawals(userDataRepository.getCredentials().address)
                .send()
        }
    }

    suspend fun getWalletBalance(): BigInteger {
        return withContext(Dispatchers.IO) {
            web3j.ethGetBalance(
                userDataRepository.getCredentials().address,
                DefaultBlockParameterName.LATEST
            ).send().balance
        }
    }

    suspend fun deliverPackage(deliveryId: BigInteger, receiverSignature: ByteArray) {
        val courierService = getCourierService()
        try {
            return withContext(Dispatchers.IO) {
                val transactionReceipt = courierService.deliverPackage(deliveryId, receiverSignature).send()
                val packageDeliveredEvents = courierService.getPackageDeliveredEvents(transactionReceipt)
                if (packageDeliveredEvents.size != 1) {
                    throw DeliverPackageError("Deliver package event not logged")
                }
            }
        } catch (e: Exception) {
            throw DeliverPackageError("Deliver package event not logged")
        }

    }
}
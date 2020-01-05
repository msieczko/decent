package eu.bwbw.decent.services

import eu.bwbw.decent.contracts.generated.CourierService
import eu.bwbw.decent.domain.Delivery
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
    credentials: Credentials
) {
    private val courierService = CourierService.load(contractAddress, web3j, credentials, DefaultGasProvider())

    suspend fun createDeliveryOffer(delivery: Delivery): BigInteger {
        val (_, title) = delivery

        val courierDeposit = toWei("1", ETHER).toBigIntegerExact()
        val courierAward = toWei("0.2", ETHER).toBigIntegerExact()
        val senderDeposit = courierDeposit.div(BigInteger.valueOf(2)).add(courierAward)

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
    }

}
package eu.bwbw.decent.domain.errors

import eu.bwbw.decent.contracts.generated.CourierService
import org.web3j.abi.datatypes.Event
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.Log
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.utils.Numeric
import java.math.BigInteger

class DecentCourierService constructor(
    contractAddress: String,
    web3j: Web3j,
    credentials: Credentials,
    contractGasProvider: ContractGasProvider
) : CourierService(
    contractAddress,
    web3j,
    credentials,
    contractGasProvider
) {
    @Suppress("RedundantOverride")
    override fun extractEventParametersWithLog(event: Event, log: Log): EventValuesWithLog? {
        return super.extractEventParametersWithLog(event, log)
    }

    @Suppress("UNCHECKED_CAST")
    fun getAllLogs(): List<Log> {
        return web3j.ethGetLogs(
            EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                Numeric.prependHexPrefix(contractAddress)
            )
        ).send().result as List<Log>
    }

    fun extractDeliveryCreatedEvents(logs: List<Log>): List<DeliveryCreatedEventResponse> =
        logs.mapNotNull { extractEventParametersWithLog(DELIVERYCREATED_EVENT, it) }
            .map {
                DeliveryCreatedEventResponse().apply {
                    log = it.log
                    deliveryId = it.indexedValues[0].value as BigInteger
                }
            }

    fun extractDeliveryCanceledEvents(logs: List<Log>): List<DeliveryCanceledEventResponse> =
        logs.mapNotNull { extractEventParametersWithLog(DELIVERYCANCELED_EVENT, it) }
            .map {
                DeliveryCanceledEventResponse().apply {
                    log = it.log
                    deliveryId = it.indexedValues[0].value as BigInteger
                }
            }

    fun extractPackagePickedUpEvents(logs: List<Log>): List<PackagePickedUpEventResponse> =
        logs.mapNotNull { extractEventParametersWithLog(PACKAGEPICKEDUP_EVENT, it) }
            .map {
                PackagePickedUpEventResponse().apply {
                    log = it.log
                    deliveryId = it.indexedValues[0].value as BigInteger
                }
            }
}
package eu.bwbw.decent.domain

import org.web3j.tuples.generated.Tuple11
import org.web3j.utils.Numeric
import java.math.BigInteger

data class ContractDelivery(
    val id: BigInteger,
    val state: DeliveryState,
    val sender: EthAddress,
    val receiver: EthAddress,
    val senderDeposit: BigInteger,
    val courierDeposit: BigInteger,
    val courierAward: BigInteger,
    val deliveryDeadline: Int,
    val pickupDeadline: Int,
    val detailsHash: String,
    val courier: EthAddress
) {
    companion object {
        fun fromTuple(
            tuple: Tuple11<
                    BigInteger,
                    BigInteger,
                    String,
                    String,
                    BigInteger,
                    BigInteger,
                    BigInteger,
                    BigInteger,
                    BigInteger,
                    ByteArray,
                    String
                    >
        ): ContractDelivery {
            return ContractDelivery(
                tuple.value1,
                DeliveryState.fromInt(tuple.value2.toInt()),
                EthAddress(tuple.value3),
                EthAddress(tuple.value4),
                tuple.value5,
                tuple.value6,
                tuple.value7,
                tuple.value8.toInt(),
                tuple.value9.toInt(),
                Numeric.toHexString(tuple.value10),
                EthAddress(tuple.value11)
            )
        }
    }
}
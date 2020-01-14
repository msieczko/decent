package eu.bwbw.decent.domain

import java.math.BigInteger

data class QrCodeData(
    val deliveryId: BigInteger,
    val signedHash: String
)
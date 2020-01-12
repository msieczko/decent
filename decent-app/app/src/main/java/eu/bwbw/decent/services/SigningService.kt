package eu.bwbw.decent.services

import eu.bwbw.decent.utils.joinSignature
import org.web3j.crypto.Credentials
import org.web3j.crypto.Sign.signPrefixedMessage
import org.web3j.utils.Numeric.hexStringToByteArray

class SigningService(private val credentials: Credentials) {
    fun signHash(hexHash: String): String {
        val signatureData = signPrefixedMessage(
            hexStringToByteArray(hexHash), credentials.ecKeyPair
        )
        return joinSignature(signatureData)
    }
}
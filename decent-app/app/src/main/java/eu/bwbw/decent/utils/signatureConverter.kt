package eu.bwbw.decent.utils

import org.web3j.crypto.Sign.SignatureData
import org.web3j.utils.Numeric.toHexString
import org.web3j.utils.Numeric.toHexStringNoPrefix

fun joinSignature(signatureData: SignatureData): String {
    return signatureData.run {
        toHexString(r) + toHexStringNoPrefix(s) + toHexStringNoPrefix(v)
    }
}
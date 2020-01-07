package eu.bwbw.decent.domain

import eu.bwbw.decent.domain.errors.InvalidAddressError
import org.web3j.crypto.Keys
import java.util.*


class EthAddress(address: String) {
    companion object {
        const val ethAddressPattern = "^(0x)?[a-fA-F0-9]{40}\$"
        const val ZERO = "0x0000000000000000000000000000000000000000"
    }

    init {
        if (!(ethAddressPattern.toRegex().matches(address))) {
            throw InvalidAddressError()
        }
    }

    val normalized: String = Keys.toChecksumAddress(address)

    val lowerCased: String
        get() = normalized.toLowerCase(Locale.ROOT)

    override fun toString() = normalized

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EthAddress

        if (normalized != other.normalized) return false

        return true
    }

    override fun hashCode(): Int {
        return normalized.hashCode()
    }
}
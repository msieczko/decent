package eu.bwbw.decent.domain

import eu.bwbw.decent.domain.errors.InvalidAddressError
import org.junit.Assert.assertEquals
import org.junit.Test

class EthAddressTest {
    @Test
    fun constructor_createsNormalizedEtherumAddressObjectFromPrefixedAddress() {
        assertEquals(
            "0x09cabEC1eAd1c0Ba254B09efb3EE13841712bE14",
            EthAddress("0x09cabec1ead1c0ba254b09efb3ee13841712be14").normalized
        )
    }

    @Test
    fun constructor_createsNormalizedEthereumAddressObjectFromUnprefixedAddress() {
        assertEquals(
            "0x09cabEC1eAd1c0Ba254B09efb3EE13841712bE14",
            EthAddress("09cabec1ead1c0ba254b09efb3ee13841712be14").normalized
        )
    }

    @Test(expected = InvalidAddressError::class)
    fun constructor_throwsExceptionForTooShortAddress() {
        EthAddress("0x09cabec1ead1c0ba254b")
    }

    @Test(expected = InvalidAddressError::class)
    fun constructor_throwsExceptionForAddressContainingInvalidCharacter() {
        EthAddress("0xG9cabEC1eAd1c0Ba254B09efb3EE13841712bE14")
    }

    @Test
    fun staticFieldZERO_hasCorrectLength() {
        assertEquals(
            "0x" + "0".repeat(40),
            EthAddress.ZERO
        )
    }

    @Test
    fun lowerCased_returnsLowerCaseEthereumAddress() {
        assertEquals(
            "0x09cabec1ead1c0ba254b09efb3ee13841712be14",
            EthAddress("0x09cabEC1eAd1c0Ba254B09efb3EE13841712bE14").lowerCased
        )
    }
}
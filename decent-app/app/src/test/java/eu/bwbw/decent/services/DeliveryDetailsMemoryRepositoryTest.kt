package eu.bwbw.decent.services

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.web3j.utils.Numeric

internal class DeliveryDetailsMemoryRepositoryTest {
    private lateinit var deliveryDetailsRepository: DeliveryDetailsMemoryRepository

    @BeforeEach
    internal fun setUp() {
        deliveryDetailsRepository = DeliveryDetailsMemoryRepository()
    }

    @Test
    fun `saves delivery details and returns hash of correct length`() {
        val deliveryDetails = DeliveryDetails("Some title", "Lorem ipsum...", "Street 1")
        val detailsHash = runBlocking {
            deliveryDetailsRepository.save(
                deliveryDetails
            )
        }
        assertEquals(32, Numeric.hexStringToByteArray(detailsHash).size)
        assertEquals(deliveryDetails, runBlocking { deliveryDetailsRepository.get(detailsHash) })
    }
}
package eu.bwbw.decent.services

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.web3j.crypto.Credentials


internal class SigningServiceTest {
    @Nested
    inner class `signHash function` {
        val privateKey = "0x706618637b8ca922f6290ce1ecd4c31247e9ab75cf0530a0ac95c0332173d7c5"
        val detailsHash = "0x6cba8c69b5f9084d8eefd5dd7cf71ed5469f5bbb9d8446533ebe4beccdfb3ce9"
        val expectedSignature = "0xd2581b67e238a2b0d782633bb4b445586b642aca779be7ea1c62a759fd9fa8a63ef97e74c6a3020780a48a12dcbc064a887371f201982250e99e79d850a2e32c1b"
        lateinit var signingService: SigningService

        @BeforeEach
        internal fun setUp() {
            val credentials = Credentials.create(privateKey)
            signingService = SigningService(credentials)
        }

        @Test
        fun `creates correct signature`() {
            assertEquals(expectedSignature, signingService.signHash(detailsHash))
        }
    }
}
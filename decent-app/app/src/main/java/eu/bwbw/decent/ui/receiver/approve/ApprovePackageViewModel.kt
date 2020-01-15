package eu.bwbw.decent.ui.receiver.approve

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import eu.bwbw.decent.domain.QrCodeData
import eu.bwbw.decent.services.CourierServiceRepository
import eu.bwbw.decent.services.SigningService
import eu.bwbw.decent.services.userdata.UserDataRepository
import java.math.BigInteger

class ApprovePackageViewModel(
    private val courierServiceRepository: CourierServiceRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    suspend fun getQrCodeData(deliveryId: BigInteger): String {
        val detailsHash = courierServiceRepository.getDetailsHash(deliveryId)
        val qrCodeData = QrCodeData(
            deliveryId = deliveryId,
            signedHash = signHash(detailsHash)
        )

        val gson = Gson()
        return gson.toJson(qrCodeData)
    }

    private fun signHash(detailsHash: String): String {
        val signingService = SigningService(userDataRepository.getCredentials())
        return signingService.signHash(detailsHash)
    }

}
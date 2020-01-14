package eu.bwbw.decent.ui.receiver.approve

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import eu.bwbw.decent.domain.QrCodeData
import eu.bwbw.decent.services.SigningService
import eu.bwbw.decent.services.userdata.UserDataRepository
import java.math.BigInteger

class ApprovePackageViewModel(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    fun getQrCodeData(deliveryId: BigInteger, deliveryDetailsHash: String): String {
        val qrCodeData = QrCodeData(
            deliveryId = deliveryId,
            signedHash = signHash(deliveryDetailsHash)
        )

        val gson = Gson()
        return gson.toJson(qrCodeData)
    }

    private fun signHash(detailsHash: String): String {
        val signingService = SigningService(userDataRepository.getCredentials())
        return signingService.signHash(detailsHash)
    }

}
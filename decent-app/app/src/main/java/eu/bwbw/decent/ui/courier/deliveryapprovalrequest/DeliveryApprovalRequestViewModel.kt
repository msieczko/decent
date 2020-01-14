package eu.bwbw.decent.ui.courier.deliveryapprovalrequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import eu.bwbw.decent.domain.QrCodeData
import eu.bwbw.decent.domain.errors.transactions.DeliverPackageError
import eu.bwbw.decent.services.CourierServiceRepository
import kotlinx.coroutines.launch
import org.web3j.utils.Numeric

class DeliveryApprovalRequestViewModel(
    private val courierServiceRepository: CourierServiceRepository
) : ViewModel() {

    protected val _deliveryRegistered = MutableLiveData<Boolean>()
    val deliveryRegistered: LiveData<Boolean>
        get() = _deliveryRegistered

    fun deliverPackage(qrCodeJson: String) {
        viewModelScope.launch {
            val gson = Gson()
            val qrCodeData = gson.fromJson<QrCodeData>(qrCodeJson, QrCodeData::class.java)

            println("id_: " + qrCodeData.deliveryId)
            println("signature_: " + qrCodeData.signedHash)

            try {
                courierServiceRepository.deliverPackage(
                    qrCodeData.deliveryId,
                    Numeric.hexStringToByteArray(qrCodeData.signedHash)
                )
            } catch (e: DeliverPackageError) {
                _deliveryRegistered.value = false
                return@launch
            }
            _deliveryRegistered.value = true
        }
    }
}
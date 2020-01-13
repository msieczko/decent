package eu.bwbw.decent.ui.sender.addnewdelivery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.bwbw.decent.domain.EthAddress
import eu.bwbw.decent.domain.errors.InvalidAddressError
import eu.bwbw.decent.services.DeliveriesService
import kotlinx.coroutines.launch
import org.web3j.utils.Convert.Unit
import org.web3j.utils.Convert.toWei
import java.math.BigInteger

data class SanitizedDelivery(
    val title: String,
    val description: String,
    val receiverEthAddress: EthAddress,
    val receiverPostalAddress: String,
    val courierDeposit: BigInteger,
    val courierAward: BigInteger,
    val maxDeliveryTime: Int
)

class AddNewDeliveryViewModel(
    private val deliveriesService: DeliveriesService
) : ViewModel() {

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val receiverEthAddress = MutableLiveData<String>()
    val receiverPostalAddress = MutableLiveData<String>()
    val courierDeposit = MutableLiveData<String>()
    lateinit var courierDepositUnit: String
    val courierAward = MutableLiveData<String>()
    lateinit var courierAwardUnit: String
    val maxDeliveryTime = MutableLiveData<String>()
    lateinit var maxDeliveryTimeUnit: String

    private val _savingData = MutableLiveData<Boolean>(false)
    val savingData: LiveData<Boolean>
        get() = _savingData

    private val _deliverySaved = MutableLiveData<Boolean>()
    val deliverySaved: LiveData<Boolean>
        get() = _deliverySaved

    private val _formValidationError = MutableLiveData<String>()
    val formValidationError: LiveData<String>
        get() = _formValidationError

    internal fun saveNewDelivery() {
        val sanitizedDelivery = sanitizeInputs() ?: return

        viewModelScope.launch {
            _savingData.value = true
            val deliveryId = deliveriesService.createDeliveryOrder(sanitizedDelivery)
            println("Created new delivery order, id=${deliveryId}")
            _savingData.value = false
            _deliverySaved.value = true
        }
    }

    private fun sanitizeInputs(): SanitizedDelivery? {
        val sTitle = title.value
        if (sTitle?.isEmpty() != false) {
            _formValidationError.value = "Title too short"
            return null
        }

        val sDescription = description.value ?: "no description"

        val sReceiverEthAddress: EthAddress
        try {
            sReceiverEthAddress = EthAddress(receiverEthAddress.value ?: "")
        } catch (e: InvalidAddressError) {
            _formValidationError.value = "Invalid receiver Ethereum address"
            return null
        }

        val sReceiverPostalAddress = receiverPostalAddress.value
        if (sReceiverPostalAddress?.isEmpty() != false) {
            _formValidationError.value = "Receiver postal address too short"
            return null
        }

        val sCourierDeposit = parseWei(courierDeposit.value, courierDepositUnit)
        if (sCourierDeposit == null || sCourierDeposit == BigInteger.ZERO) {
            _formValidationError.value = "Invalid courier deposit value"
            return null
        }

        val sCourierAward = parseWei(courierAward.value, courierAwardUnit)
        if (sCourierAward == null || sCourierAward == BigInteger.ZERO) {
            _formValidationError.value = "Invalid courier award value"
            return null
        }

        val sMaxDeliveryTime = parseTime(maxDeliveryTime.value, maxDeliveryTimeUnit)
        if (sMaxDeliveryTime == null || sMaxDeliveryTime == 0) {
            _formValidationError.value = "Invalid max delivery time"
            return null
        }

        return SanitizedDelivery(
            sTitle,
            sDescription,
            sReceiverEthAddress,
            sReceiverPostalAddress,
            sCourierDeposit,
            sCourierAward,
            sMaxDeliveryTime
        )
    }

    private fun parseWei(value: String?, unit: String): BigInteger? {
        return try {
            toWei(value, Unit.fromString(unit)).toBigIntegerExact()
        } catch (e: Exception) {
            null
        }
    }

    private fun parseTime(value: String?, unit: String): Int? {
        try {
            val number = value?.toInt() ?: return null
            return when (unit) {
                "hours" -> number * 3600
                "days" -> number * 3600 * 24
                else -> null
            }
        } catch (e: Exception) {
            return null
        }
    }
}

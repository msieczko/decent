package eu.bwbw.decent.ui.sender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.services.CourierServiceRepository
import kotlinx.coroutines.launch
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j

class AddNewDeliveryViewModel(
    private val courierServiceContractAddress: String,
    private val web3j: Web3j
) : ViewModel() {
    val title = MutableLiveData<String>()


    private val _savingData = MutableLiveData<Boolean>()
    val savingData: LiveData<Boolean>
        get() = _savingData

    private val _deliverySaved = MutableLiveData<Boolean>()
    val deliverySaved: LiveData<Boolean>
        get() = _deliverySaved

    private val _formValidationError = MutableLiveData<String>()
    val formValidationError: LiveData<String>
        get() = _formValidationError


    internal fun saveNewDelivery(credentials: Credentials) {
        val currentTitle = title.value

        if (currentTitle == null || currentTitle.isEmpty()) {
            _formValidationError.value = "Too short title"
            return
        }


        val delivery = Delivery(
            0,
            currentTitle,
            "r",
            "",
            "Warszawa",
            0,
            0,
            0
        )

        _savingData.value = true
        viewModelScope.launch {
            val courierServiceRepository = CourierServiceRepository(courierServiceContractAddress, web3j, credentials)
            val deliveryId = courierServiceRepository.createDeliveryOrder(delivery)
            println(deliveryId)
            _savingData.postValue(false)
            _deliverySaved.postValue(true)
        }
    }
}

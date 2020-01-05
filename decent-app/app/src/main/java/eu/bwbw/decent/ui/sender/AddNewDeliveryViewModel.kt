package eu.bwbw.decent.ui.sender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.bwbw.decent.services.DeliveriesRepository
import eu.bwbw.decent.domain.Delivery
import kotlinx.coroutines.launch

class AddNewDeliveryViewModel(private val deliveriesRepository: DeliveriesRepository) :
    ViewModel() {
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


    internal fun saveNewDelivery() {
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
            deliveriesRepository.saveDelivery(delivery)
            _savingData.postValue(false)
            _deliverySaved.postValue(true)
        }
    }
}

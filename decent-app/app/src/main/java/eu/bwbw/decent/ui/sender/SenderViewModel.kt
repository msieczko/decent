package eu.bwbw.decent.ui.sender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.bwbw.decent.DeliveriesRepository
import eu.bwbw.decent.domain.Delivery

class SenderViewModel(
    private val deliveriesRepository: DeliveriesRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is sender Fragment"
    }
    val text: LiveData<String> = _text

    fun getDeliveries() : List<Delivery> {
        return deliveriesRepository.getDeliveries()
    }

    fun onRemoveDeliveryClick(deliveryId: Int) {
        deliveriesRepository.remove(deliveryId)
    }
}
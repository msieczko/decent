package eu.bwbw.decent.ui.sender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.bwbw.decent.services.DeliveriesRepository
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel

class SenderViewModel(
    private val deliveriesRepository: DeliveriesRepository
) : BaseDeliveriesViewModel(deliveriesRepository) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is sender Fragment"
    }
    val text: LiveData<String> = _text

    fun onRemoveDeliveryClick(deliveryId: Int) {
        deliveriesRepository.remove(deliveryId)
        _deliveriesUpdated.value = true
    }
}
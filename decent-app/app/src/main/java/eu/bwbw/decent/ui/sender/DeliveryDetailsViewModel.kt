package eu.bwbw.decent.ui.sender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.bwbw.decent.DeliveriesRepository
import eu.bwbw.decent.domain.Delivery

class DeliveryDetailsViewModel(
    private val deliveriesRepository: DeliveriesRepository
) : ViewModel() {

    private val _delivery = MutableLiveData<Delivery>()
    val delivery: LiveData<Delivery>
        get() = _delivery

    fun openDelivery(deliveryId: Int) {
        deliveriesRepository.getDelivery(
            deliveryId
        ) {
            _delivery.value = it
        }
    }
}

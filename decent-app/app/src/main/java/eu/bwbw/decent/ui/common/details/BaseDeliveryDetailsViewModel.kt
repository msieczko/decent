package eu.bwbw.decent.ui.common.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.services.DeliveriesService
import java.math.BigInteger

abstract class BaseDeliveryDetailsViewModel(
    private val deliveriesService: DeliveriesService
) : ViewModel() {

    private val _delivery = MutableLiveData<Delivery>()
    val delivery: LiveData<Delivery>
        get() = _delivery

    fun openDelivery(deliveryId: BigInteger) {
        _delivery.value = deliveriesService.getDelivery(deliveryId)
    }
}

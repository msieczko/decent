package eu.bwbw.decent.ui.common.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.domain.DeliveryState
import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.utils.DateConverters
import java.math.BigInteger

abstract class BaseDeliveryDetailsViewModel(
    private val deliveriesService: DeliveriesService
) : ViewModel() {

    protected val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    protected val _actionFinished = MutableLiveData<Boolean>()
    val actionFinished: LiveData<Boolean>
        get() = _actionFinished

    private val _delivery = MutableLiveData<Delivery>()
    val delivery: LiveData<Delivery>
        get() = _delivery

    fun openDelivery(deliveryId: BigInteger) {
        _delivery.value = deliveriesService.getDelivery(deliveryId)
    }

    fun getDeliveryTime(): String {
        delivery.value?.let {
            return when (delivery.value?.state) {
                DeliveryState.OFFER, DeliveryState.PICKUP_DECLARED, DeliveryState.OFFER_CANCELED
                -> "Max delivery time: ${DateConverters.secondsToHours(it.deliveryDeadline)}"
                else -> "Delivery deadline: ${DateConverters.secondsToDateTimeString(it.deliveryDeadline)}"
            }
        }
        return ""
    }


}

package eu.bwbw.decent.ui.sender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.services.IUserDataRepository
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger

class SenderViewModel(
    userDataRepository: IUserDataRepository,
    private val deliveriesService: DeliveriesService
) : BaseDeliveriesViewModel(userDataRepository) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is sender Fragment"
    }

    val text: LiveData<String> = _text
    fun onRemoveDeliveryClick(deliveryId: BigInteger) {
        viewModelScope.launch {
            deliveriesService.cancelDeliveryOrder(deliveryId)
            super.removeDelivery(deliveryId)
            _deliveriesUpdated.value = true
            updateDeliveries()
        }
    }

    override suspend fun getDeliveries(): List<Delivery> {
        _isLoading.value = true

        val elements = deliveriesService.getSenderDeliveries()
        this.deliveries.clear()
        this.deliveries.addAll(elements)

        _isLoading.value = false
        return this.deliveries
    }
}

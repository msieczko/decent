package eu.bwbw.decent.ui.sender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel
import org.web3j.crypto.Credentials
import java.math.BigInteger

class SenderViewModel(
    private val deliveriesService: DeliveriesService
) : BaseDeliveriesViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is sender Fragment"
    }

    val text: LiveData<String> = _text
    fun onRemoveDeliveryClick(deliveryId: BigInteger) {
        deliveriesService.remove(deliveryId)
        _deliveriesUpdated.value = true
    }

    override suspend fun getDeliveries(credentials: Credentials): List<Delivery> {
        this.deliveries.clear()
        this.deliveries.addAll(deliveriesService.getSenderDeliveries(credentials))
        return this.deliveries
    }
}

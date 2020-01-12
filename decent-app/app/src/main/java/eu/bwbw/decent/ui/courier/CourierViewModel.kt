package eu.bwbw.decent.ui.courier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel
import org.web3j.crypto.Credentials

class CourierViewModel(
    private val deliveriesService: DeliveriesService
) : BaseDeliveriesViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is courier Fragment"
    }
    val text: LiveData<String> = _text

    override suspend fun getDeliveries(): List<Delivery> {
        _isLoading.value = true

        this.deliveries.clear()
        this.deliveries.addAll(deliveriesService.getCourierDeliveries())

        _isLoading.value = false
        return this.deliveries
    }
}
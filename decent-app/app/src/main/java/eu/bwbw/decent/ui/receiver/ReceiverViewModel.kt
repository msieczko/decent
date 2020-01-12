package eu.bwbw.decent.ui.receiver

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.services.userdata.IUserDataRepository
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel

class ReceiverViewModel(
    private val userDataRepository: IUserDataRepository,
    private val deliveriesService: DeliveriesService
) : BaseDeliveriesViewModel(userDataRepository) {

    private val _text = MutableLiveData<String>().apply {
        value = "My Eth address: ${userDataRepository.getCredentials().address}"
    }
    val text: LiveData<String> = _text

    override suspend fun getDeliveries(): List<Delivery> {
        _isLoading.value = true

        this.deliveries.clear()
        this.deliveries.addAll(deliveriesService.getReceiverDeliveries())

        _isLoading.value = false
        return this.deliveries
    }

}

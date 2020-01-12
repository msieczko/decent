package eu.bwbw.decent.ui.courier.details

import eu.bwbw.decent.services.CourierServiceRepository
import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.ui.common.details.BaseDeliveryDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DeliveryDetailsCourierViewModel(
    private val courierServiceRepository: CourierServiceRepository,
    deliveriesService: DeliveriesService
) : BaseDeliveryDetailsViewModel(deliveriesService) {

    fun pickupPackage() {
        // using viewmodel scope would cancel the job after leaving the view
        CoroutineScope(IO).launch {
            delivery.value?.let {
                _isLoading.postValue(true)
                courierServiceRepository.pickupPackage(it.id)
                _actionFinished.postValue(true)
                _isLoading.postValue(false)
            }
        }
    }

}

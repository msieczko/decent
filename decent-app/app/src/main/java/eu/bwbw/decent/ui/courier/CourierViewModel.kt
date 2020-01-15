package eu.bwbw.decent.ui.courier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.bwbw.decent.R
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.services.userdata.IUserDataRepository
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel
import kotlinx.android.synthetic.main.authentication_fragment.*

class CourierViewModel(
    userDataRepository: IUserDataRepository,
    private val deliveriesService: DeliveriesService
) : BaseDeliveriesViewModel(userDataRepository) {

    override suspend fun getDeliveries(): List<Delivery> {
        _isLoading.value = true

        this.deliveries.clear()
        this.deliveries.addAll(deliveriesService.getCourierDeliveries())

        _isLoading.value = false
        return this.deliveries
    }
}
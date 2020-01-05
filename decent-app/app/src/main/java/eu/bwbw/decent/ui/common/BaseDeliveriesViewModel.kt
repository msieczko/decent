package eu.bwbw.decent.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.bwbw.decent.DeliveriesRepository
import eu.bwbw.decent.domain.Delivery

open class BaseDeliveriesViewModel(
    private val deliveriesRepository: DeliveriesRepository
) : ViewModel() {

    protected val _deliveriesUpdated = MutableLiveData<Boolean>()
    val deliveriesUpdated: LiveData<Boolean>
        get() = _deliveriesUpdated

    fun getDeliveries() : List<Delivery> {
        return deliveriesRepository.getDeliveries()
    }

    fun updateDeliveries() {
        deliveriesRepository.getDeliveries()
        _deliveriesUpdated.value = true
    }
}

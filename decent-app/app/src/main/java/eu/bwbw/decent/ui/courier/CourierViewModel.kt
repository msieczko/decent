package eu.bwbw.decent.ui.courier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.bwbw.decent.DeliveriesRepository
import eu.bwbw.decent.domain.Delivery

class CourierViewModel(
    private val deliveriesRepository: DeliveriesRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is courier Fragment"
    }
    val text: LiveData<String> = _text

    fun getDeliveries() : List<Delivery> {
        return deliveriesRepository.getDeliveries()
    }
}
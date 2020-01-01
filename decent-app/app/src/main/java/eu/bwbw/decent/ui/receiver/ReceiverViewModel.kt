package eu.bwbw.decent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.bwbw.decent.DeliveriesRepository
import eu.bwbw.decent.domain.Delivery

class ReceiverViewModel(
    private val deliveriesRepository: DeliveriesRepository

) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "My Eth address: ..."
    }
    val text: LiveData<String> = _text

    fun getDeliveries() : List<Delivery> {
        return deliveriesRepository.getDeliveries()
    }
}
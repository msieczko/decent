package eu.bwbw.decent.ui.receiver

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.bwbw.decent.DeliveriesRepository
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel

class ReceiverViewModel(
    private val deliveriesRepository: DeliveriesRepository

) : BaseDeliveriesViewModel(deliveriesRepository) {

    private val _text = MutableLiveData<String>().apply {
        value = "My Eth address: ..."
    }
    val text: LiveData<String> = _text

}
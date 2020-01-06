package eu.bwbw.decent.ui.courier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.bwbw.decent.services.DeliveriesRepository
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel

class CourierViewModel(
    private val deliveriesRepository: DeliveriesRepository
) : BaseDeliveriesViewModel(deliveriesRepository) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is courier Fragment"
    }
    val text: LiveData<String> = _text

}
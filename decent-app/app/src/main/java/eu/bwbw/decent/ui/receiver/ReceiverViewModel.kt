package eu.bwbw.decent.ui.receiver

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.bwbw.decent.domain.Delivery
import eu.bwbw.decent.services.DeliveriesService
import eu.bwbw.decent.ui.common.BaseDeliveriesViewModel
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import java.math.BigInteger

class ReceiverViewModel(
    private val deliveriesService: DeliveriesService
) : BaseDeliveriesViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "My Eth address: ..."
    }

    val text: LiveData<String> = _text
    var privateKey: String = ""
        set(key) {
            field = key
            val keyPair = ECKeyPair.create(BigInteger(key, 16))
            _text.value = Keys.getAddress(keyPair)
        }

    override suspend fun getDeliveries(): List<Delivery> {
        _isLoading.value = true

        this.deliveries.clear()
        this.deliveries.addAll(deliveriesService.getReceiverDeliveries())

        _isLoading.value = false
        return this.deliveries
    }

}

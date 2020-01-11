package eu.bwbw.decent.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.bwbw.decent.domain.Delivery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.web3j.crypto.Credentials
import java.math.BigInteger

abstract class BaseDeliveriesViewModel : ViewModel() {
    internal var deliveries: ArrayList<Delivery> = ArrayList()

    protected val _deliveriesUpdated = MutableLiveData<Boolean>()
    val deliveriesUpdated: LiveData<Boolean>
        get() = _deliveriesUpdated

    abstract suspend fun getDeliveries(credentials: Credentials): List<Delivery>

    fun updateDeliveries(credentials: Credentials) {
        CoroutineScope(Dispatchers.Default).launch {
            getDeliveries(credentials)
            _deliveriesUpdated.postValue(true)
        }
    }

    protected fun removeDelivery(deliveryId: BigInteger) {
        deliveries.removeAll { delivery -> delivery.id == deliveryId }
        _deliveriesUpdated.value = true
    }
}

package eu.bwbw.decent.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.bwbw.decent.domain.Delivery
import org.web3j.crypto.Credentials

abstract class BaseDeliveriesViewModel : ViewModel() {

    protected val _deliveriesUpdated = MutableLiveData<Boolean>()
    val deliveriesUpdated: LiveData<Boolean>
        get() = _deliveriesUpdated

    abstract suspend fun getDeliveries(credentials: Credentials): List<Delivery>

    suspend fun updateDeliveries(credentials: Credentials) {
        this.getDeliveries(credentials)
        _deliveriesUpdated.value = true
    }
}
